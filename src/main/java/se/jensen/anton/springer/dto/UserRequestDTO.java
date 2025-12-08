package se.jensen.anton.springer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import se.jensen.anton.springer.custombeans.PasswordRequirement;

public record UserRequestDTO(@NotBlank String username,

                             @Email
                             @NotBlank String email,

                             @NotBlank @PasswordRequirement String password,

                             @NotBlank String role,

                             @NotBlank String displayName,

                             @NotBlank String bio,
                             
                             String profileImagePath) {
}
