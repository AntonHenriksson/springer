package se.jensen.anton.springer.mapper;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.FriendshipRespondDTO;
import se.jensen.anton.springer.model.Friendship;

@Component
public class FriendMapper {


    public FriendshipRespondDTO toDto(Friendship friendship) {
        return new FriendshipRespondDTO(
                friendship.getId(),
                friendship.getSender().getId(),
                friendship.getReceiver().getId(),
                friendship.getStatus()
        );
    }

}
