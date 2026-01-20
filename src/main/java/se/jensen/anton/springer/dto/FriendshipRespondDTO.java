package se.jensen.anton.springer.dto;

import se.jensen.anton.springer.model.Friendship;

public record FriendshipRespondDTO(Long id,
                                   Long sender,
                                   Long receiver,
                                   Friendship.Status status) {
}
