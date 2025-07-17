package finki.ukim.mk.diplomska.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public UUID id;
    public String postTitle;
    public String description;
    public LocalDateTime dateTimeCreation;
    public String imageName;
    public String imageType;
    public boolean donateMoney;
    public Double moneyDonationLimit;

    @Lob
    public byte[] imageData;

    public Integer likes;

    @Enumerated(EnumType.STRING)
    public PostCategory postCategory;

    @Enumerated(EnumType.STRING)
    public DonationCategory donationCategory;

    @ManyToOne
    ApplicationUser user;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Comment> comments ;


    public Post(String postTitle, String description, ApplicationUser user) {
        this.postTitle = postTitle;
        this.description = description;
        this.dateTimeCreation = LocalDateTime.now();
        this.likes = 0;
        this.user = user;
        this.comments =  new ArrayList<>();
    }

    public Post(String postTitle, String description, PostCategory postCategory, DonationCategory donationCategory, ApplicationUser user) {
        this.postTitle = postTitle;
        this.description = description;
        this.postCategory = postCategory;
        this.donationCategory = donationCategory;
        this.dateTimeCreation = LocalDateTime.now();
        this.likes = 0;
        this.user = user;
        this.comments =  new ArrayList<>();

    }

    public Post(String postTitle, String description,boolean donateMoney, Double moneyDonationLimit, PostCategory postCategory, DonationCategory donationCategory, ApplicationUser user) {
        this.postTitle = postTitle;
        this.description = description;
        this.dateTimeCreation = LocalDateTime.now();
        this.donateMoney = donateMoney;
        this.moneyDonationLimit = moneyDonationLimit;
        this.likes = 0;
        this.postCategory = postCategory;
        this.donationCategory = donationCategory;
        this.user = user;
        this.comments =  new ArrayList<>();

    }
}
