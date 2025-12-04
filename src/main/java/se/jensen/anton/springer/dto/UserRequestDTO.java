package se.jensen.anton.springer.dto;

import jakarta.validation.constraints.NotBlank;
import se.jensen.anton.springer.custombeans.PasswordRequirement;

public record UserRequestDTO(@NotBlank String username,
                             @NotBlank @PasswordRequirement String password) {
}
