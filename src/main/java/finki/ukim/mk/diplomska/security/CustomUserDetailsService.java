package finki.ukim.mk.diplomska.security;

import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.repository.ApplicationUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final ApplicationUserRepository userRepository;


    public CustomUserDetailsService(ApplicationUserRepository userRepository ) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ApplicationUser user = userRepository
                .findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found!"));

        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority(user.getRole().toString()));
        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
