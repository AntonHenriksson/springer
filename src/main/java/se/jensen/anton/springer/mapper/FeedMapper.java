package se.jensen.anton.springer.mapper;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.FeedResponseDTO;
import se.jensen.anton.springer.dto.PostResponseDTO;
import se.jensen.anton.springer.model.Post;

@Component
public class FeedMapper {

    /**
     * This method converts {@link Post} entity to a {@link FeedResponseDTO}
     *
     * @param post {@link Post} entity instance to be converted
     * @return {@link FeedResponseDTO} containing the post's id, text, creation timestamp, the post's author id, the author's username and the author's display name
     */
    public FeedResponseDTO toDto(Post post) {
        return new FeedResponseDTO(
                post.getId(),
                post.getText(),
                post.getCreated(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getUser().getDisplayName()
        );

    }
}
