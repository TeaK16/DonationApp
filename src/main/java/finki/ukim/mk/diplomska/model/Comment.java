package finki.ukim.mk.diplomska.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public UUID id;
    public String description;
    public Integer likes;
    @ManyToOne
    ApplicationUser user;
    @ManyToOne
    Post post;


    public Comment(String description, ApplicationUser user, Post post) {
        this.description = description;
        this.likes = 0;
        this.user = user;
        this.post = post;
    }


}
