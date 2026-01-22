package se.jensen.anton.springer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * DTO for updating a user's info
 *
 * @param username         the user's username (max 50 characters)
 * @param email            the user's email address (max 60 characters)
 * @param displayName      the user's display name (max 30 characters)
 * @param bio              the user's biography (max 200 characters)
 * @param profileImagePath the path to the profile image (max 100 characters)
 */
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
