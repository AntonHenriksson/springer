package se.jensen.anton.springer.dto;

import se.jensen.anton.springer.model.Friendship;

/**
 * This DTO contains friendship details for API responses.
 *
 * @param id                  ID of the friend request
 * @param sender              The name of the sender
 * @param receiver            The name of the receiver
 * @param status              Status of the friendship request
 * @param senderDisplayName   Display name of the sender
 * @param receiverDisplayName Display name of the receiver
 */
public record FriendshipRespondDTO(Long id,
                                   Long sender,
                                   Long receiver,
                                   Friendship.Status status,
                                   String senderDisplayName,
                                   String receiverDisplayName) {
}
