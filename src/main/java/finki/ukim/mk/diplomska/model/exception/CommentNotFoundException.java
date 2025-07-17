package finki.ukim.mk.diplomska.model.exception;

import java.util.UUID;

public class CommentNotFoundException extends Exception{
    public CommentNotFoundException() {
    }

    public CommentNotFoundException(UUID uuid) {
        super(String.format("Comment with id: %s not found.", uuid));
    }
}
