package finki.ukim.mk.diplomska.service;

import finki.ukim.mk.diplomska.model.Comment;
import finki.ukim.mk.diplomska.model.Post;
import finki.ukim.mk.diplomska.model.dto.CommentDto;
import finki.ukim.mk.diplomska.model.exception.CommentNotFoundException;
import finki.ukim.mk.diplomska.model.exception.PostNotFoundException;
import finki.ukim.mk.diplomska.model.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    List<Comment> listCommentsByPost(UUID uuid) throws PostNotFoundException;
    Comment addComment(CommentDto commentDto) throws PostNotFoundException, UserNotFoundException;
    Comment editComment(UUID uuid, CommentDto commentDto) throws CommentNotFoundException;
    Comment deleteComment(UUID uuid) throws CommentNotFoundException;

    public void like(UUID uuid) throws CommentNotFoundException;

}
