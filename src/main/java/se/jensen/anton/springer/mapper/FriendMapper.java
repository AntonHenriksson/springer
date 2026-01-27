package se.jensen.anton.springer.mapper;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.FriendshipRespondDTO;
import se.jensen.anton.springer.model.Friendship;
import se.jensen.anton.springer.model.Post;

/**
 * Mapper component for converting between {@link FriendshipRespondDTO} and {@link Friendship} entities.
 * * The mapper focuses on data transformation.
 */
@Component
public class FriendMapper {


    /**
     * This method converts {@link Friendship} entity to a {@link FriendshipRespondDTO}
     *
     * @param friendship {@link Friendship} entity instance to be converted
     * @return {@link FriendshipRespondDTO} containing ID of the friendship, the sender's id, the receiver's id, status of friendship, the receiver's displayName, and the sender's displayName
     */
    public FriendshipRespondDTO toDto(Friendship friendship) {
        return new FriendshipRespondDTO(
                friendship.getId(),
                friendship.getSender().getId(),
                friendship.getReceiver().getId(),
                friendship.getStatus(),
                friendship.getReceiver().getDisplayName(),
                friendship.getSender().getDisplayName()
        );
    }

}
