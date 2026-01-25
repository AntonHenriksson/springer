package se.jensen.anton.springer.mapper;


import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.PostRequestDTO;
import se.jensen.anton.springer.dto.PostResponseDTO;
import se.jensen.anton.springer.dto.UserUpdateRequestDTO;
import se.jensen.anton.springer.model.Post;
import se.jensen.anton.springer.model.User;

/**
 * Mapper component for converting between Post-related DTOs and {@link Post} entities.
 * The mapper focuses on data transformation.
 */
@Component
public class PostMapper {

    /**
     * This method converts {@link PostRequestDTO} to a {@link Post} entity
     *
     * @param dto {@link PostRequestDTO} to be converted to a {@link Post} entity
     * @return {@link Post} entity to be stored in the database
     */
    public Post fromDto(PostRequestDTO dto) {
        Post post = new Post();
        updateEntity(dto, post);
        return post;
    }

    /**
     * This method converts {@link Post} entity to a {@link PostResponseDTO}
     *
     * @param post {@link Post} entity instance to be converted
     * @return {@link PostResponseDTO} containing text, creation timestamp and id
     */
    public PostResponseDTO toDto(Post post) {
        return new PostResponseDTO(
                post.getText(),
                post.getCreated(),
                post.getId()
        );
    }

    /**
     * This method updates a {@link Post} entity with values from {@link PostRequestDTO} values
     * It is used when updating an existing post.
     * All new values from the DTO overwrite the corresponding fields of the entity.
     *
     * @param dto  {@link PostRequestDTO} containing updated post data
     * @param post the existing {@link Post} entity to be updated
     */
    public void updateEntity(PostRequestDTO dto, Post post) {
        post.setCreated(dto.created());
        post.setText(dto.text());
    }
}
