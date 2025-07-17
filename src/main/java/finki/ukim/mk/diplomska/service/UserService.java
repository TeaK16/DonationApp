package finki.ukim.mk.diplomska.service;


import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.dto.LoginEmailDto;
import finki.ukim.mk.diplomska.model.dto.LoginUsernameDto;
import finki.ukim.mk.diplomska.model.dto.UserDto;
import finki.ukim.mk.diplomska.model.exception.*;

import java.util.List;
import java.util.UUID;

public interface UserService {
    ApplicationUser findUserByUsername(String username) throws UsernameNotFoundException;
    ApplicationUser findUserByEmail(String email) throws EmailNotFoundException;
    void registerUser(UserDto userDto) throws UsernameExistsException, EmailExistsException, PasswordDoesNotMatchPattern;

    ApplicationUser findById(UUID id) throws UserNotFoundException;
    List<ApplicationUser>  searchUsernameBySubstring(String substring);
    ApplicationUser findUserByStripeCustomerId(String stripeCustomerID);

}
