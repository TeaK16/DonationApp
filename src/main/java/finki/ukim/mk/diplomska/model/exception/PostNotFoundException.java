package finki.ukim.mk.diplomska.model.exception;

import java.util.UUID;

public class PostNotFoundException extends Exception{
    public PostNotFoundException() {
    }

    public PostNotFoundException(UUID uuid) {
        super(String.format("Post with id: %s is not found", uuid));
    }
}
