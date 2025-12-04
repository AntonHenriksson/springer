package se.jensen.anton.springer.mapper;


import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.PostRequestDTO;
import se.jensen.anton.springer.dto.PostRespondDTO;
import se.jensen.anton.springer.model.Post;

@Component
public class PostMapper {

    public Post toPost(PostRequestDTO postRequestDTO) {
        Post post = new Post();
        post.setText(postRequestDTO.text());
        post.setCreated(postRequestDTO.created());
        //inget id här?
        return post;
    }

    public PostRespondDTO toPostRespondDTO(Post post) {
        return new PostRespondDTO(
                post.getText(),
                post.getCreated(),
                post.getId()
        );
    }
}
