package finki.ukim.mk.diplomska.repository;

import finki.ukim.mk.diplomska.model.Comment;
import finki.ukim.mk.diplomska.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findCommentsByPost(Post post);
}
