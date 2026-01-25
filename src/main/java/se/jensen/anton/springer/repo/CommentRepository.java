package se.jensen.anton.springer.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.anton.springer.model.Comment;
import se.jensen.anton.springer.model.Post;


/**
 * Repository interface for managing Comment entities.
 * This repository extends {@link JpaRepository} to provide CRUD-methods.
 * It includes methods to find comments associated with a specific post's ID.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Get a paginated list of comments for the specific post identified by postId
     *
     * @param postId   ID of the post whose comments are to be fetched
     * @param pageable Pagination information including page number, size, and sort order
     * @return a paginated {@link Page} of {@link Comment} objects which consists of id, text, and creation timestamp, userId and username
     */
    Page<Comment> findByPostId(
            Long postId,
            Pageable pageable);
}
