package finki.ukim.mk.diplomska.service.impl;

import finki.ukim.mk.diplomska.model.Comment;
import finki.ukim.mk.diplomska.model.Post;
import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.dto.CommentDto;
import finki.ukim.mk.diplomska.model.exception.CommentNotFoundException;
import finki.ukim.mk.diplomska.model.exception.PostNotFoundException;
import finki.ukim.mk.diplomska.model.exception.UserNotFoundException;
import finki.ukim.mk.diplomska.repository.CommentRepository;
import finki.ukim.mk.diplomska.repository.PostRepository;
import finki.ukim.mk.diplomska.repository.ApplicationUserRepository;
import finki.ukim.mk.diplomska.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    public final CommentRepository commentRepository;
    public final PostRepository postRepository;
    public final ApplicationUserRepository userRepository;

    public CommentServiceImpl( CommentRepository commentRepository, PostRepository postRepository, ApplicationUserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Comment> listCommentsByPost(UUID uuid) throws PostNotFoundException {
        List<Comment> comments = new ArrayList<>();
        Post post = postRepository.findById(uuid).orElseThrow(PostNotFoundException::new);
        comments  = commentRepository.findCommentsByPostOrderByDateTimeCreationDesc(post);
        return comments;
    }

    @Override
    public Comment addComment(CommentDto commentDto) throws PostNotFoundException, UserNotFoundException {
        ApplicationUser user = userRepository.findById(commentDto.getUserUUID()).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(commentDto.getPostUUID()).orElseThrow(PostNotFoundException::new);
        Comment comment = new Comment(commentDto.getDescription(), user, post);
        commentRepository.save(comment);
        return comment;

    }

    @Override
    public Comment editComment(UUID uuid, CommentDto commentDto) throws CommentNotFoundException {
         Comment comment = commentRepository.findById(uuid).orElseThrow(()-> new CommentNotFoundException(uuid));
         comment.setDescription(commentDto.getDescription());
         commentRepository.save(comment);
         return comment;
    }

    @Override
    public Comment deleteComment(UUID uuid) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(uuid).orElseThrow(() -> new  CommentNotFoundException(uuid));
        commentRepository.delete(comment);
        return comment;
    }

    @Override
    public void like(UUID uuid) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(uuid).orElseThrow(() -> new CommentNotFoundException(uuid));
        Integer numLikes = comment.getLikes() + 1;
        comment.setLikes(numLikes);
        commentRepository.save(comment);
    }
}
