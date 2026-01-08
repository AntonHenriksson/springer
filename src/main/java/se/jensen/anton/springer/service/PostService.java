package se.jensen.anton.springer.service;

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

    public PostService(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
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
    public List<PostResponseDTO> findAll() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toDto)
                .toList();
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
