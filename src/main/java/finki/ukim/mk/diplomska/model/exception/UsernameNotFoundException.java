package finki.ukim.mk.diplomska.model.exception;

public class UsernameNotFoundException extends Exception{
    public UsernameNotFoundException() {
    }

    public UsernameNotFoundException(String username) {
        super(String.format("User with username: %s not found.", username));
    }
}
