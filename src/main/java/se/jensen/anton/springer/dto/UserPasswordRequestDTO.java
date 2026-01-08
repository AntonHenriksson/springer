package se.jensen.anton.springer.dto;

import jakarta.validation.constraints.NotBlank;
import se.jensen.anton.springer.custombeans.PasswordRequirement;

public record UserPasswordRequestDTO(@NotBlank
                                     @PasswordRequirement
                                     String password) {
}
