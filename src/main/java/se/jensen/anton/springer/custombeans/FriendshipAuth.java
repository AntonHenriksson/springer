package se.jensen.anton.springer.custombeans;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.repo.FriendshipRepository;
import se.jensen.anton.springer.security.SecurityUtils;

@Component("friendshipAuth")
public class FriendshipAuth {

    private final FriendshipRepository friendshipRepository;

    public FriendshipAuth(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public boolean isReceiver(Long friendshipId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId != null && friendshipId != null) {
            return friendshipRepository.findById(friendshipId)
                    .map(friendship ->
                            friendship.getReceiver() != null &&
                                    friendship.getReceiver().getId().equals(currentUserId))
                    .orElse(false);
        }
        return false;
    }

}
