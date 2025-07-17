package finki.ukim.mk.diplomska.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.DonationStatus;
import finki.ukim.mk.diplomska.model.MoneyDonation;
import finki.ukim.mk.diplomska.model.Post;
import finki.ukim.mk.diplomska.model.dto.DonationProgressDto;
import finki.ukim.mk.diplomska.model.dto.MoneyDonationRequestDto;
import finki.ukim.mk.diplomska.model.exception.PostNotFoundException;
import finki.ukim.mk.diplomska.repository.ApplicationUserRepository;
import finki.ukim.mk.diplomska.repository.MoneyDonationRepository;
import finki.ukim.mk.diplomska.service.MoneyDonationService;
import finki.ukim.mk.diplomska.service.PostService;
import finki.ukim.mk.diplomska.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class MoneyDonationServiceImpl implements MoneyDonationService {

    private final MoneyDonationRepository moneyDonationRepository;
    private final PostService postService;
    private final UserService userService;

    private final ApplicationUserRepository applicationUserRepository;


    public MoneyDonationServiceImpl(MoneyDonationRepository moneyDonationRepository, PostService postService,UserService userService, ApplicationUserRepository applicationUserRepository) {
        this.moneyDonationRepository = moneyDonationRepository;
        this.postService = postService;
        this.userService = userService;
        this.applicationUserRepository = applicationUserRepository;
    }


        @Override
        public Map<String, String> createPaymentSheet(MoneyDonationRequestDto moneyDonationRequestDto, ApplicationUser user) throws StripeException {

            try {

               Post post = postService.findById(UUID.fromString(moneyDonationRequestDto.getPostId()));

               Customer customer = createOrGetCustomer(user);

                EphemeralKey ephemeralKey = EphemeralKey.create(
                        Map.of("customer", customer.getId()),
                        RequestOptions.builder().setStripeVersionOverride("2025-04-30.basil").build());

//                EphemeralKey ephemeralKey = EphemeralKey.create(
//                        Map.of(
//                                "customer", customer.getId(),
//                                "stripe_version", "2023-10-16"
//                        )
//                );

                PaymentIntent paymentIntent = PaymentIntent.create(Map.of(
                        "amount", moneyDonationRequestDto.getAmount(),
                        "currency", moneyDonationRequestDto.getCurrency(),
                        "customer", customer.getId(),
                        "automatic_payment_methods", Map.of("enabled", true),
                        "metadata", Map.of("postName", post.getPostTitle()
                        )));

                MoneyDonation moneyDonation = new MoneyDonation(user,moneyDonationRequestDto.getAmount()/100, moneyDonationRequestDto.getCurrency(), paymentIntent.getId(),post);

                moneyDonationRepository.save(moneyDonation);

                return Map.of(
                        "paymentIntent", paymentIntent.getClientSecret(),
                        "ephemeralKey", ephemeralKey.getSecret(),
                        "customer", customer.getId()
                );

            } catch (PostNotFoundException e) {
                throw new RuntimeException(e);
            }


        }

    public Optional<MoneyDonation> findByTransactionalId(String transactionId) {
        return moneyDonationRepository.findByTransactionId(transactionId);
    }

    private Customer createOrGetCustomer(ApplicationUser user) throws StripeException {
            if(user.getStripeCustomerId() != null){
                return Customer.retrieve(user.getStripeCustomerId());
            }
            Customer customer = Customer.create(Map.of(
                    "name", user.getUsername(),
                    "email", user.getEmail()
            ));

            user.setStripeCustomerId(customer.getId());
            applicationUserRepository.save(user);

            return customer;

        }

    @Override
    public List<MoneyDonation> findBySuccessfulTransByPost(UUID postId) {
        Post post = null;
        try {
            post = postService.findById(postId);
        } catch (PostNotFoundException e) {
            throw new RuntimeException(e);
        }

        return moneyDonationRepository.findByPostAndDonationStatus(post, DonationStatus.SUCCESS);
    }

    @Override
    public DonationProgressDto moneyDonationProgress(UUID postId) {

        Post post = null;
        try {
            post = postService.findById(postId);
        } catch (PostNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<MoneyDonation> moneyDonations = moneyDonationRepository.findByPostAndDonationStatus(post, DonationStatus.SUCCESS);

        Double totalDonatedMoney =  moneyDonations.stream().mapToDouble(MoneyDonation::getAmount).sum();

        return new DonationProgressDto(post.getMoneyDonationLimit(), totalDonatedMoney);
    }
}
