package se.jensen.anton.springer.dto;

import java.util.List;

public record UserWithPostsResponseDTO(
        UserResponseDTO user,
        List<PostResponseDTO> posts) {
}