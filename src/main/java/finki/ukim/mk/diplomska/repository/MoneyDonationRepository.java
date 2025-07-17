package finki.ukim.mk.diplomska.repository;

import finki.ukim.mk.diplomska.model.DonationStatus;
import finki.ukim.mk.diplomska.model.MoneyDonation;
import finki.ukim.mk.diplomska.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface MoneyDonationRepository extends JpaRepository<MoneyDonation, UUID> {
    Optional<MoneyDonation> findByTransactionId(String transactionalId);
    List<MoneyDonation> findByPostAndDonationStatus(Post post, DonationStatus donationStatus);
    List<MoneyDonation>  findByPost(Post post);
}
