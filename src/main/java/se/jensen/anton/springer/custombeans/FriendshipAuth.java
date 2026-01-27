package se.jensen.anton.springer.custombeans;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.repo.FriendshipRepository;
import se.jensen.anton.springer.security.SecurityUtils;

/**
 * Custom bean for handling authorization checks related to friendships.
 */
@Component("friendshipAuth")
public class FriendshipAuth {

    private final FriendshipRepository friendshipRepository;

    /**
     * This method constructs a FriendshipAuth with the specified repository.
     *
     * @param friendshipRepository The repository used to access friendship data
     */
    public FriendshipAuth(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    /**
     * This method checks whether the currently authenticated user is the receiver of the friendship with the given ID.
     *
     * @param friendshipId The ID of the friendship to check
     * @return True: if the current user is the receiver of the friendship. False: if the user is not the receiver, the friendship does not exist, or if either the current user ID or friendship ID is null.
     */
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
