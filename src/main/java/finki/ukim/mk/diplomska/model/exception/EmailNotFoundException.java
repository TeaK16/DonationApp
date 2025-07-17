package finki.ukim.mk.diplomska.model.exception;

public class EmailNotFoundException extends Exception {
    public EmailNotFoundException() {
    }

    public EmailNotFoundException(String email) {
        super(String.format("User with email: %s not found.", email));
    }
}

