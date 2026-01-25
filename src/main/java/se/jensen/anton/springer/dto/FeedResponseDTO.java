package se.jensen.anton.springer.dto;

import java.time.LocalDateTime;

/**
 * This DTO represents a post, which contains the post's info and the author's info, in the feed
 *
 * @param id          the post's id
 * @param text        the post's text
 * @param created     the timestamp when the post is created
 * @param userId      ID of the user who created the post
 * @param username    the username of the user who created the post
 * @param displayName the display name of the user who created the post
 */
public record FeedResponseDTO(
        Long id,
        String text,
        LocalDateTime created,
        Long userId,
        String username,
        String displayName

) {
}
