package se.jensen.anton.springer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.anton.springer.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
