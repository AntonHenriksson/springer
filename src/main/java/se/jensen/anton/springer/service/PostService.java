package se.jensen.anton.springer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import se.jensen.anton.springer.dto.PostRequestDTO;
import se.jensen.anton.springer.dto.PostResponseDTO;
import se.jensen.anton.springer.mapper.PostMapper;
import se.jensen.anton.springer.model.Post;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.PostRepository;
import se.jensen.anton.springer.repo.UserRepository;
import se.jensen.anton.springer.security.SecurityUtils;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    public PostService(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

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


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<PostResponseDTO> findAll() {
        logger.debug("Finding all posts");
        return postRepository.findAll()
                .stream()
                .map(postMapper::toDto)
                .toList();
    }

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
