package se.jensen.anton.springer.mapper;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.FeedPostDTO;
import se.jensen.anton.springer.model.Post;

@Component
public class FeedMapper {

    public FeedPostDTO toDto(Post post) {
        return new FeedPostDTO(
                post.getId(),
                post.getText(),
                post.getCreated(),
                post.getUser().getUsername()
        );
    }
}
