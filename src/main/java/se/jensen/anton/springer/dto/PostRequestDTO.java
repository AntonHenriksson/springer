package se.jensen.anton.springer.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record PostRequestDTO(
        @NotBlank String text,
        LocalDateTime created) {
}



