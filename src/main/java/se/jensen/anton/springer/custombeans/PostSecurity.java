package se.jensen.anton.springer.custombeans;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.repo.PostRepository;
import se.jensen.anton.springer.security.SecurityUtils;

/**
 * Security helper component for performing post-related authorization checks.
 */
@Component("postSecurity")
public class PostSecurity {

    private final PostRepository postRepository;

    /**
     * This method constructs a {@link PostSecurity} instance.
     *
     * @param postRepository The repository used to access post data.
     */
    public PostSecurity(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * This method evaluates if the currently authenticated user identified by the given postId is the owner of the specified post.
     *
     * @param postId ID of the post
     * @return True: if the current user is the owner of the post. False: if the user is not the owner.
     */
    public boolean isOwner(Long postId) {
        String username = SecurityUtils.getCurrentUsername();
        return username != null && postRepository.existsByIdAndUser_username(postId, username);
    }


}
