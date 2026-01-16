package se.jensen.anton.springer.dto;

import java.time.LocalDateTime;

public record FeedResponseDTO<T>(
        Long id,
        String text,
        LocalDateTime created,
        String username,
        String displayName

) {
}
