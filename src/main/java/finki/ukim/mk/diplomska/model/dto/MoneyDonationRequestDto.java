package finki.ukim.mk.diplomska.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MoneyDonationRequestDto {
    public UUID userId;
    public String postId;
    public Long amount;
    public String currency;

}
