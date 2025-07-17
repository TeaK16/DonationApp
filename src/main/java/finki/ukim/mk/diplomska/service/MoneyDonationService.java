package finki.ukim.mk.diplomska.service;

import com.stripe.exception.StripeException;
import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.MoneyDonation;
import finki.ukim.mk.diplomska.model.dto.DonationProgressDto;
import finki.ukim.mk.diplomska.model.dto.MoneyDonationRequestDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MoneyDonationService {
    public Map<String,String> createPaymentSheet(MoneyDonationRequestDto moneyDonationRequestDto, ApplicationUser user) throws StripeException;
    public Optional<MoneyDonation> findByTransactionalId(String transactionalId);
    public List<MoneyDonation> findBySuccessfulTransByPost(UUID postId);

    public DonationProgressDto moneyDonationProgress(UUID postId);




}
