package finki.ukim.mk.diplomska.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "appUser")
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public UUID uuid;
    public String username;
    public String firstName;
    public String lastName;
    public String organizationName;
    public String email;
    public String password;
    public LocalDate dayOfBirth;
    public String stripeCustomerId;

    @Enumerated(value = EnumType.STRING)
    public Role role;



    public ApplicationUser(String username, String firstName, String lastName, String organizationName, String email, String password, LocalDate dayOfBirth, String stripeCustomerId, Role role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organizationName = organizationName;
        this.email = email;
        this.password = password;
        this.dayOfBirth = dayOfBirth;
        this.stripeCustomerId = stripeCustomerId;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
