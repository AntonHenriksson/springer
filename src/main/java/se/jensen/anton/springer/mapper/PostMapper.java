package se.jensen.anton.springer.mapper;


import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.PostRequestDTO;
import se.jensen.anton.springer.dto.PostResponseDTO;
import se.jensen.anton.springer.model.Post;

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
     * @return {@link PostResponseDTO} containing post data which consists of text, creation timestamp and id
     */
    public PostResponseDTO toDto(Post post) {
        return new PostResponseDTO(
                post.getText(),
                post.getCreated(),
                post.getId()
        );
    }

    /**
     *
     * @param dto
     * @param post
     */
    public void updateEntity(PostRequestDTO dto, Post post) {
        post.setCreated(dto.created());
        post.setText(dto.text());
    }
}
