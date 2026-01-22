package se.jensen.anton.springer.dto;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        String role,
        String displayName,
        String bio,
        String profileImagePath) {
}
