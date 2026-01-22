package se.jensen.anton.springer.dto;

import jakarta.validation.constraints.NotBlank;
import se.jensen.anton.springer.custombeans.PasswordRequirement;

/**
 * DTO for updating a user's password
 *
 * @param password the user's new password. This cannot be blank.
 */
public record UserPasswordRequestDTO(@NotBlank
                                     @PasswordRequirement
                                     String password) {
}
