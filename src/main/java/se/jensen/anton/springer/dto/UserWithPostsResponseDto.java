package se.jensen.anton.springer.dto;

import java.util.List;

public record UserWithPostsResponseDto(
        UserResponseDTO user,
        List<PostResponseDTO> posts) {
}