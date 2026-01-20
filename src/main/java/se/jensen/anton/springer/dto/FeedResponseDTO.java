package se.jensen.anton.springer.dto;

import java.time.LocalDateTime;

public record FeedResponseDTO(
        Long id,
        String text,
        LocalDateTime created,
        Long userId,
        String username,
        String displayName

) {
}
