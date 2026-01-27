package se.jensen.anton.springer.dto;

/**
 * This DTO represents a login request for existing users to login, including username and password required for authentication
 *
 * @param username the user's username stored in database
 * @param password the user's password stored in database
 */
public record LoginRequestDTO(String username, String password) {
}
