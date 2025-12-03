package se.jensen.anton.springer.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(@NotBlank String username,
                             @NotBlank String password) {
}
