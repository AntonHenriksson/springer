package se.jensen.anton.springer.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.anton.springer.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsByIdAndUserId(Long postId, Long userId);

    boolean existsByIdAndUser_username(Long postId, String username);

    Page<Post> findByUserId(Long userId, Pageable pageable);
}
