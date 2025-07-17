package finki.ukim.mk.diplomska.model.dto;


import finki.ukim.mk.diplomska.model.DonationCategory;
import finki.ukim.mk.diplomska.model.PostCategory;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    public String postTitle;
    public String description;
    public PostCategory postCategory;
    public DonationCategory donationCategory;
    public UUID userUUID;
    @Getter
    public boolean donateMoney;
    public Double moneyDonationLimit;

}
