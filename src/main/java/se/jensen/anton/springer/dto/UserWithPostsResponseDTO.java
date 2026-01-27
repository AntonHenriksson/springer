package se.jensen.anton.springer.dto;

import java.util.List;

/**
 * This DTO used to return a user along with all posts which the user has created
 *
 * @param user  This user's information including id, username, email, role, displayName and profileImagePath
 * @param posts a list of post, and each post contains text, creation timestamp and id
 */
public record UserWithPostsResponseDTO(
        UserResponseDTO user,
        List<PostResponseDTO> posts) {
}