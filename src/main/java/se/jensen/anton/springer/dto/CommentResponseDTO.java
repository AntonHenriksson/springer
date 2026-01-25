package se.jensen.anton.springer.dto;

import java.time.LocalDateTime;

/**
 * This DTO represents a comment response which includes id, text, created, userId and username
 *
 * @param id       ID of the newly created comment
 * @param text     Text of the comment
 * @param created  Timestamp when the comment was made
 * @param userId   ID of the author who created the comment
 * @param username The author's username
 */
public record CommentResponseDTO(
        Long id,
        String text,
        LocalDateTime created,

        Long userId,
        String username
) {
}
