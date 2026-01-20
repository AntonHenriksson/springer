package se.jensen.anton.springer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.anton.springer.model.Friendship;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findBySenderIdOrReceiverId(Long senderId, Long receiverId);

}
