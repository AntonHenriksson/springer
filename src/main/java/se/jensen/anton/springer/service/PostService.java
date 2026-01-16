package se.jensen.anton.springer.service;

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

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final FeedMapper feedMapper;

    public PostService(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper, FeedMapper feedMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.feedMapper = feedMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public PostResponseDTO findById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            Post realPost = post.get();
            return postMapper.toDto(realPost);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Page<FeedResponseDTO> getGlobalFeed(int page, int size) {
        PageRequest pageRequest =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"));
        return postRepository.findAll(pageRequest)
                .map(feedMapper::toDto);
    }

    public Page<FeedResponseDTO> getMyWall(String username, int page, int size) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"));
        return postRepository.findByUserId(user.getId(), pageable)
                .map(feedMapper::toDto);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isOwner(#id)")
    public PostResponseDTO updatePost(Long id, PostRequestDTO dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        postMapper.updateEntity(dto, post);
        return postMapper.toDto(post);
    }


    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public PostResponseDTO addPost(PostRequestDTO dto) {

        Post post = postMapper.fromDto(dto);
        User currentUser = SecurityUtils.requireCurrentUser(userRepository);
        post.setUser(currentUser);
        postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isOwner(#id)")
    public void deletePost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.deleteById(id);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
    }
}
