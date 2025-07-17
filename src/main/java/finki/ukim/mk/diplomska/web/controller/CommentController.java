package finki.ukim.mk.diplomska.web.controller;

import finki.ukim.mk.diplomska.model.Comment;
import finki.ukim.mk.diplomska.model.dto.CommentDto;
import finki.ukim.mk.diplomska.model.exception.CommentNotFoundException;
import finki.ukim.mk.diplomska.model.exception.PostNotFoundException;
import finki.ukim.mk.diplomska.model.exception.UserNotFoundException;
import finki.ukim.mk.diplomska.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public List<Comment> listCommentsByPostId(@PathVariable UUID id)
    {
        try {
            return commentService.listCommentsByPost(id);
        } catch (PostNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCommentToPost(@RequestBody CommentDto commentDto)
    {
        try {
            Comment comment = commentService.addComment(commentDto);
            return ResponseEntity.ok(comment);
        } catch (PostNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }

    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editComment(@PathVariable UUID id,@RequestBody CommentDto commentDto){
        try {
            commentService.editComment(id,commentDto);
        } catch (CommentNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>("The comment is edited.", HttpStatus.OK);

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID id)
    {
        try {
            commentService.deleteComment(id);
        } catch (CommentNotFoundException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>("Comment deleted successfully.",HttpStatus.OK);
    }

    @PostMapping("/like/{id}")
    public void likeComment(@PathVariable UUID id){
        try {
            commentService.like(id);
        } catch (CommentNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
