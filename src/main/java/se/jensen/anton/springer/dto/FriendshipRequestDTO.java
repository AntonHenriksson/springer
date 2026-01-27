package se.jensen.anton.springer.dto;


/**
 * This DTO is intended for creating or updating a friendship request
 *
 * @param senderId   ID of the request sender
 * @param receiverId ID of the request receiver
 */
public record FriendshipRequestDTO(Long senderId, Long receiverId) {
}
