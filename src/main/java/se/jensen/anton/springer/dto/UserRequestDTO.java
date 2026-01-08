package se.jensen.anton.springer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import se.jensen.anton.springer.custombeans.PasswordRequirement;

public record UserRequestDTO(@NotBlank
                             @Size(max = 50)
                             String username,
                             @Size(max = 60)
                             @Email
                             @NotBlank String email,
                             @Size(max = 100)
                             @NotBlank @PasswordRequirement String password,
                             @Size(max = 20)
                             @NotBlank String role,
                             @Size(max = 30)
                             @NotBlank String displayName,
                             @Size(max = 200)
                             @NotBlank String bio,
                             @Size(max = 100)
                             String profileImagePath) {
}
