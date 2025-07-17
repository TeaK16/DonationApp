package finki.ukim.mk.diplomska.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DonationProgressDto {
    Double donationLimit;
    Double donatedAmount;

}
