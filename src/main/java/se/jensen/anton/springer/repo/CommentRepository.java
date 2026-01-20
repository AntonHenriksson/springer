package se.jensen.anton.springer.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.anton.springer.model.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByPostId(
            Long postId,
            Pageable pageable);
}
