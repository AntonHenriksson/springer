package se.jensen.anton.springer.dto;

import java.time.LocalDateTime;

public record CommentResponseDTO(
        Long id,
        String text,
        LocalDateTime created,

        Long userId,
        String username
) {
}
