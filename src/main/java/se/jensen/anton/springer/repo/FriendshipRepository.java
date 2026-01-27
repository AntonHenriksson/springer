package se.jensen.anton.springer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.anton.springer.model.Friendship;

import java.util.List;

/**
 * Repository interface for accessing {@link Friendship} entities from the database.
 */
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findBySenderIdOrReceiverId(Long senderId, Long receiverId);

}
