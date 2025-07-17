package finki.ukim.mk.diplomska.model.exception;

public class PasswordDoesNotMatchPattern extends Exception{
    public PasswordDoesNotMatchPattern() {
        super("Password must contain at least one special sine, at least one uppercase letter or lowercase letter and number");
    }

}
