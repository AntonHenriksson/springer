package se.jensen.anton.springer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(
        @Size(max = 50)
        String username,
        @Email
        @Size(max = 60)
        String email,
        @Size(max = 30)
        String displayName,
        @Size(max = 200)
        String bio,
        @Size(max = 100)
        String profileImagePath) {
}

//limited this, consider if username should be changed or not
//will let user change email without checks for demo. later should have validation of new
