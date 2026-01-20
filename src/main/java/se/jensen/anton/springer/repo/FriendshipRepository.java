package se.jensen.anton.springer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.anton.springer.model.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
}
