package se.jensen.anton.springer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import se.jensen.anton.springer.dto.FeedResponseDTO;
import se.jensen.anton.springer.dto.PostRequestDTO;
import se.jensen.anton.springer.dto.PostResponseDTO;
import se.jensen.anton.springer.mapper.FeedMapper;
import se.jensen.anton.springer.mapper.PostMapper;
import se.jensen.anton.springer.model.Post;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.PostRepository;
import se.jensen.anton.springer.repo.UserRepository;
import se.jensen.anton.springer.security.SecurityUtils;

import java.util.Optional;

/**
 * Service layer for handling {@link Post} related business logic.
 */
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final FeedMapper feedMapper;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    public PostService(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper, FeedMapper feedMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.feedMapper = feedMapper;
    }

    /**
     * This method fetches a post identified by its ID
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param id ID of the post to fetch
     * @return {@link PostResponseDTO} containing the post data
     * @throws ResponseStatusException with HTTP status 404 if no post with the given ID exists
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public PostResponseDTO findById(Long id) {
        logger.debug("Finding post with id={}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    logger.debug("Post not found, id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
                });

        logger.debug("Post found, id={}", post.getId());
        return postMapper.toDto(post);
    }


    /**
     * This method fetches a paginated list of posts identified by the given userId.
     * If a user ID is provided, only posts created by the user will be returned.
     *
     * @param page   Page index
     * @param size   The number of posts per page
     * @param userId Optional user ID to filter posts by author of the posts
     * @return {@link Page} of {@link FeedResponseDTO}
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Page<FeedResponseDTO> getPosts(int page, int size, Optional<Long> userId) {
        if (userId.isPresent()) {
            return postRepository.findByUserId(userId.get(), PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created")))
                    .map(feedMapper::toDto);
        }
        PageRequest pageRequest =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"));
        return postRepository.findAll(pageRequest)
                .map(feedMapper::toDto);
    }

    /**
     * This method fetches a paginated feed for the specified user.
     * The feed contains posts created by the user, which are sorted by creation time in descending order.
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param username The name of the user whose feed is fetched
     * @param page     Page index
     * @param size     The number of posts per page
     * @return {@link Page} of {@link FeedResponseDTO}
     * @throws ResponseStatusException with HTTP status 404 if no user with the given username found.
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Page<FeedResponseDTO> getMyWall(String username, int page, int size) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"));
        return postRepository.findByUserId(user.getId(), pageable)
                .map(feedMapper::toDto);
    }

    /**
     * This method updated an existing post.
     * Access is allowed to users with either the ADMIN-role or the post's author
     *
     * @param id  ID of the post to update
     * @param dto The new post data which includes updated inputs
     * @return {@link PostResponseDTO} containing the updated post data
     * @throws ResponseStatusException with HTTP 404 status
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isOwner(#id)")
    public PostResponseDTO updatePost(Long id, PostRequestDTO dto) {
        logger.debug("Updating post with id={}", id);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    logger.debug("Post not found, id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
                });

        postMapper.updateEntity(dto, post);
        logger.info("Post updated, id={}", post.getId());
        return postMapper.toDto(post);
    }


    /**
     * This method creates a new post
     * The post is automatically related with the currently authenticated user.
     * Access is allowed to users with either the ADMIN- or USER-role.
     *
     * @param dto {@link PostRequestDTO}
     * @return {@link PostResponseDTO} object containing a new post data
     */
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public PostResponseDTO addPost(PostRequestDTO dto) {
        logger.debug("Adding new post");
        Post post = postMapper.fromDto(dto);
        User currentUser = SecurityUtils.requireCurrentUser(userRepository);
        post.setUser(currentUser);
        postRepository.save(post);
        logger.info("Post added, id={}", post.getId());
        return postMapper.toDto(post);
    }

    /**
     * This method deletes a post by its ID.
     * Access is allowed to users with either the ADMIN-role or the post's author
     *
     * @param id ID of the post to delete
     * @throws ResponseStatusException with HTTP 404 status
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isOwner(#id)")
    public void deletePost(Long id) {
        logger.debug("Deleting post with id={}", id);
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.deleteById(id);
            logger.info("Post deleted, id={}", id);
            return;
        }
        logger.debug("Post not found, id={}", id);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
    }
}
