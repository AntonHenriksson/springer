package se.jensen.anton.springer.dto;

/**
 * This DTO contains user details for API responses.
 * The object contains all user's information except the user's password
 *
 * @param id               the user's ID
 * @param username         the user's username
 * @param email            the user's email address
 * @param role             the user's role in this app (e.g. ADMIN, USER)
 * @param displayName      the user's display name
 * @param bio              the user's biography
 * @param profileImagePath the path to the profile image
 */
public record UserResponseDTO(
        Long id,
        String username,
        String email,
        String role,
        String displayName,
        String bio,
        String profileImagePath) {
}
