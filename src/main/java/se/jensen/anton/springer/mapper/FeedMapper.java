package se.jensen.anton.springer.mapper;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.FeedResponseDTO;
import se.jensen.anton.springer.model.Post;

@Component
public class FeedMapper {

    public FeedResponseDTO toDto(Post post) {
        return new FeedResponseDTO(
                post.getId(),
                post.getText(),
                post.getCreated(),
                post.getUser().getUsername()
        );
    }
}
