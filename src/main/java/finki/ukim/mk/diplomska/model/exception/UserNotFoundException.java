package finki.ukim.mk.diplomska.model.exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException() {
        super("User not found!");
    }
}
