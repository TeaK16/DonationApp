package finki.ukim.mk.diplomska.service.impl;

import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.dto.LoginEmailDto;
import finki.ukim.mk.diplomska.model.dto.LoginUsernameDto;
import finki.ukim.mk.diplomska.model.dto.UserDto;
import finki.ukim.mk.diplomska.model.exception.*;
import finki.ukim.mk.diplomska.repository.ApplicationUserRepository;
import finki.ukim.mk.diplomska.service.UserService;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationUserServiceImpl implements UserService {
    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApplicationUserServiceImpl(ApplicationUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public ApplicationUser findUserByUsername(String username) throws UsernameNotFoundException {
          ApplicationUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));


        return user;

    }


    @Override
    public ApplicationUser findUserByEmail(String email) throws EmailNotFoundException {
        ApplicationUser user =  userRepository.findByEmail(email).orElseThrow(()->new EmailNotFoundException(email));

        return user;
    }

    //@Transactional
    @Override
    public ApplicationUser findById(UUID id) throws UserNotFoundException {
        ApplicationUser user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        return user;
    }

    @Override
    public void registerUser(UserDto userDto) throws UsernameExistsException, EmailExistsException, PasswordDoesNotMatchPattern {
        Optional<ApplicationUser> userByUsername = userRepository.findByUsername(userDto.getUsername());
        Optional<ApplicationUser> userByEmail = userRepository.findByEmail(userDto.getEmail());

        if (!userByUsername.isEmpty()) {
            throw new UsernameExistsException(userDto.getUsername());
        }
        if (!userByEmail.isEmpty()) {
            throw new EmailExistsException(userDto.getEmail());

        }

        ApplicationUser user = new ApplicationUser();


        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setOrganizationName(userDto.getOrganizationName());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setDayOfBirth(userDto.getDayOfBirth());
        user.setRole(userDto.getRole());

        String password = userDto.getPassword();
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

        if (password.matches(pattern)) {
            user.setPassword(passwordEncoder.encode(password));
        } else {
            throw new PasswordDoesNotMatchPattern();
        }

        userRepository.save(user);

    }

    @Override
    public List<ApplicationUser> searchUsernameBySubstring(String substring){
        return userRepository.findByUsernameContainingIgnoreCase(substring);
    }

    @Override
    public ApplicationUser findUserByStripeCustomerId(String stripeCustomerID) {
        Optional<ApplicationUser> user = userRepository.findByStripeCustomerId(stripeCustomerID);
        ApplicationUser user1 = null;
        if(user.isPresent()) {
            user1 = user.get();
        }
        return user1;
    }
}
