package se.jensen.anton.springer.custombeans;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.repo.PostRepository;
import se.jensen.anton.springer.security.SecurityUtils;

@Component
public class PostSecurity {

    private final PostRepository postRepository;

    public PostSecurity(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public boolean isOwner(Long postId) {
        Long userId = SecurityUtils.getCurrentUserId();
        return postRepository.existsByIdAndUserId(postId, userId);
    }
}
