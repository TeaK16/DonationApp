package finki.ukim.mk.diplomska.web.controller;

import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.Role;
import finki.ukim.mk.diplomska.model.dto.LoginEmailDto;
import finki.ukim.mk.diplomska.model.dto.UserDto;
import finki.ukim.mk.diplomska.model.exception.*;
import finki.ukim.mk.diplomska.security.CustomUserDetailsService;
import finki.ukim.mk.diplomska.security.JwtService;
import finki.ukim.mk.diplomska.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:8081")
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/user")
public class AuthUserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;


    public AuthUserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;

    }



    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto)
    {
        try {
            userService.registerUser(userDto);
        } catch (UsernameExistsException | PasswordDoesNotMatchPattern e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>("User registered successfully.", HttpStatus.OK);
    }

    @GetMapping("/register/roles")
    public List<Role> listUserRoles(){ return List.of(Role.values()); }

    @PostMapping("/login")
    public ResponseEntity<?> loginAndGetToken(@RequestBody LoginEmailDto loginEmailDto) throws UserNotFoundException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginEmailDto.getEmail(),
                loginEmailDto.getPassword()));
        try {
            ApplicationUser applicationUser = userService.findUserByEmail(loginEmailDto.getEmail());
            if(authentication.isAuthenticated()){
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtService
                        .generatedToken(customUserDetailsService
                                        .loadUserByUsername(loginEmailDto.getEmail()), applicationUser.getUuid());

                return new ResponseEntity<>(token,HttpStatus.OK);

            }else {
                throw new UserNotFoundException();
            }
        } catch (EmailNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
//        String token = extractTokenFromRequest(request);

        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("Logged out successfully.", HttpStatus.OK);
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }


}
