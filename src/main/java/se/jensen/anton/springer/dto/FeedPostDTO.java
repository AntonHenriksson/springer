package se.jensen.anton.springer.dto;

import java.time.LocalDateTime;

public record FeedPostDTO(Long id, String text, LocalDateTime created, String username) {
}
