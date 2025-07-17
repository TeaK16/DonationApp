package finki.ukim.mk.diplomska.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
public class MoneyDonation {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;
    @ManyToOne
    private ApplicationUser user;
    private Long amount;
    private String currency;
    private LocalDateTime donationDate;
    private String transactionId;
    @Enumerated(EnumType.STRING)
    private DonationStatus donationStatus;
    @ManyToOne
    private Post post;

    public MoneyDonation(ApplicationUser user, Long amount, String currency, String transactionId, Post post) {
        this.user = user;
        this.amount = amount;
        this.currency = currency;
        this.donationDate = LocalDateTime.now();
        this.transactionId = transactionId;
        this.donationStatus = DonationStatus.PENDING;
        this.post = post;
    }
}
