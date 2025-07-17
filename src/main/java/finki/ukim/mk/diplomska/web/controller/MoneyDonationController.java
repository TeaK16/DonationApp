package finki.ukim.mk.diplomska.web.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.DonationStatus;
import finki.ukim.mk.diplomska.model.MoneyDonation;
import finki.ukim.mk.diplomska.model.dto.DonationProgressDto;
import finki.ukim.mk.diplomska.model.dto.MoneyDonationRequestDto;
import finki.ukim.mk.diplomska.model.exception.EmailNotFoundException;
import finki.ukim.mk.diplomska.repository.MoneyDonationRepository;
import finki.ukim.mk.diplomska.service.MailService;
import finki.ukim.mk.diplomska.service.MoneyDonationService;
import finki.ukim.mk.diplomska.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/donate/money")
public class MoneyDonationController {
    private final MoneyDonationService moneyDonationService;
    private final MoneyDonationRepository moneyDonationRepository;
    private final UserService userService;
    private final MailService mailService;

    public MoneyDonationController(MoneyDonationService moneyDonationService, MoneyDonationRepository moneyDonationRepository, UserService userService, MailService mailService) {
        this.moneyDonationService = moneyDonationService;
        this.userService = userService;
        this.moneyDonationRepository = moneyDonationRepository;
        this.mailService = mailService;

    }


    @PostMapping("/checkout")
    public ResponseEntity<Map<String,String>> createPaymentSheet(@RequestBody MoneyDonationRequestDto moneyDonationRequestDto, Principal principal) throws StripeException {

        try {
            ApplicationUser user = userService.findUserByEmail(principal.getName());
            Map<String, String> response = moneyDonationService.createPaymentSheet(moneyDonationRequestDto, user);
            return ResponseEntity.ok(response);
        } catch (EmailNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

//    stripe listen --forward-to localhost:8080/api/donate/money/webhook
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        String endpointSecret = "whsec_b5b607eadd9c00550228443eddec48ec85c0095e9e7b8294f70a12f2b0b20a79";
        Event event;


        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            System.out.printf("Webhook signature verification failed %s", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

            try {
                StripeObject stripeObject = dataObjectDeserializer.deserializeUnsafe();
                if (stripeObject instanceof PaymentIntent paymentIntent) {
                    String transactionId = paymentIntent.getId();
                    System.out.println(paymentIntent);
                    System.out.println("PaymentIntent ID: " + transactionId);
                    System.out.println("Customer ID: " + paymentIntent.getCustomer());

                    ApplicationUser user = userService.findUserByStripeCustomerId(paymentIntent.getCustomer());

                    Optional<MoneyDonation> optionalDonation = moneyDonationService.findByTransactionalId(transactionId);
                    if (optionalDonation.isPresent()) {
                        MoneyDonation donation = optionalDonation.get();
                        donation.setDonationStatus(DonationStatus.SUCCESS);
                        mailService.sendDonationConfirmation(user.getEmail(), "Successful donation", "Dear, "+user.getEmail()+"\nThank you for supporting " + paymentIntent.getMetadata().get("postName") +"!");
                        moneyDonationRepository.save(donation);
                    } else {
                        System.out.println("Donation not found for: " + transactionId);
                    }

                } else {
                    System.err.println("Unexpected object type in event data: " + stripeObject.getClass().getName());
                }

            } catch (Exception ex){
                System.err.println("Failed to deserialize PaymentIntent: " + ex.getMessage());
            }


            }else {
                return ResponseEntity.ok().build();
            }




        return ResponseEntity.ok("Webhook handled");
    }

    @GetMapping("/successdonations/{id}")
    public List<MoneyDonation> getSuccessfulDonations(@PathVariable String id){
        return moneyDonationService.findBySuccessfulTransByPost(UUID.fromString(id));
    }

    @GetMapping("/progress/{id}")
    public ResponseEntity<DonationProgressDto> getDonationProgress(@PathVariable String id){
        DonationProgressDto donationProgressDto =  moneyDonationService.moneyDonationProgress(UUID.fromString(id));
        return ResponseEntity.ok(donationProgressDto);
    }


}
