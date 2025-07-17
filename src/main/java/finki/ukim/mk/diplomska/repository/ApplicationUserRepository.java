package finki.ukim.mk.diplomska.repository;


import finki.ukim.mk.diplomska.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {
    Optional<ApplicationUser> findByUsername(String username);
    Optional<ApplicationUser> findByEmail(String email);
    List<ApplicationUser> findByUsernameContainingIgnoreCase(String serchString);

    Optional<ApplicationUser> findByStripeCustomerId(String stripeCustomerId);
  ;
}
