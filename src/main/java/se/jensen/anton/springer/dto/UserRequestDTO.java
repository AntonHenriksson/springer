package se.jensen.anton.springer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import se.jensen.anton.springer.custombeans.PasswordRequirement;

/**
 * This DTO is used for creating a new user or updating existing users' info.
 * It consists of username, email, password, role, displayName, bio and profileImagePath.
 * {@link jakarta.validation.Valid} validates each field when the request is processed.
 *
 * @param username         The user's username. his value can contain a maximum of 50 characters and must not be blank.
 * @param email            The user's email address. This value must be a valid email format, can contain a maximum of 60 characters, and must not be blank.
 * @param password         The user's password. This value can contain a maximum of 100 characters, must not be blank, and must satisfy the password requirements.
 * @param role             The user's role in this app (e.g. ADMIN, USER). This value can contain a maximum of 20 characters and must not be blank.
 * @param displayName      The user's display name. This value can contain a maximum of 30 characters and must not be blank.
 * @param bio              The user's biography. This value can contain a maximum of 200 characters and must not be blank.
 * @param profileImagePath The path to the profile image. This value can contain a maximum of 100 characters.
 */
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
