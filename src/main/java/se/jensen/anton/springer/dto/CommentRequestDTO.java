package se.jensen.anton.springer.dto;


import jakarta.validation.constraints.PastOrPresent;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

/**
 * This DTO represents a request to create a comment, and it includes text and the timestamp when the comment was made
 *
 * @param text    Text of the comment and this must not be null.
 * @param created The timestamp when the comment was created. This must not be null and is in the past or present
 */
public record CommentRequestDTO(
        @NonNull String text,
        @NonNull @PastOrPresent LocalDateTime created
) {
}
