package se.jensen.anton.springer.dto;

import java.util.List;

public record UserWithPostsResponseDto(
        UserRespondDTO user,
        List<PostRespondDTO> posts) {
}