package finki.ukim.mk.diplomska.model.exception;

public class UsernameExistsException extends Exception{

    public UsernameExistsException() {
    }

    public UsernameExistsException(String username) {
        super(String.format("User with username: %s, already exists", username));
    }
}
