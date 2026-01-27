package se.jensen.anton.springer.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.anton.springer.model.Post;

/**
 * Repository interface for managing Post entities.
 * This repository extends {@link JpaRepository} to provide CRUD-methods.
 * It includes methods to verify post ownership and get posts created by a specific user.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsByIdAndUserId(Long postId, Long userId);

    /**
     * Check whether the post with the given ID exists and belongs to the user with the given username
     *
     * @param postId   ID of the post
     * @param username The username of the author
     * @return True: if the posts exists and belongs to the user. False: otherwise.
     */
    boolean existsByIdAndUser_username(Long postId, String username);

    /**
     * Get all posts created by a specific user identified by the id
     *
     * @param userId   ID of the post's author whose posts are to be fetched
     * @param pageable Pagination information including page number, size, and sort order
     * @return a paginated {@link Page} of {@link Post} objects which consists of id, text, creation timestamp and user's info
     */
    Page<Post> findByUserId(Long userId, Pageable pageable);
}
