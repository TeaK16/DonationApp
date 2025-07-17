package finki.ukim.mk.diplomska.model.exception;

public class EmailExistsException extends RuntimeException{

    public EmailExistsException() {
    }

    public EmailExistsException(String email) {
        super(String.format("User with email: %s, already exists", email ));
    }
}
