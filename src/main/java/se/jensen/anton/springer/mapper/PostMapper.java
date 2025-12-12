package se.jensen.anton.springer.mapper;


import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.PostRequestDTO;
import se.jensen.anton.springer.dto.PostRespondDTO;
import se.jensen.anton.springer.model.Post;

@Component
public class PostMapper {

    public Post fromDto(PostRequestDTO dto) {
        Post post = new Post();
        updateEntity(dto, post);
        return post;
    }

    public PostRespondDTO toDto(Post post) {
        return new PostRespondDTO(
                post.getText(),
                post.getCreated(),
                post.getId()
        );
    }

    public void updateEntity(PostRequestDTO dto, Post post) {
        post.setCreated(dto.created());
        post.setText(dto.text());
    }
}
