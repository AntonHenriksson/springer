package se.jensen.anton.springer.dto;

/**
 * This DTO represents the response which will be returned after the user has been successfully logged in, including token and userId
 *
 * @param token  JWT token which will be generated for the authenticated user
 * @param userId userId which has been automatically created in database upon creating the user
 */
public record LoginResponseDTO(String token, Long userId) {
}
