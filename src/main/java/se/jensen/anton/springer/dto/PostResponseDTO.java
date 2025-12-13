package se.jensen.anton.springer.dto;

import java.time.LocalDateTime;

public record PostResponseDTO(String text, LocalDateTime created, Long id) {

}
