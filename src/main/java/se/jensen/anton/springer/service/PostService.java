package se.jensen.anton.springer.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.jensen.anton.springer.dto.PostRequestDTO;
import se.jensen.anton.springer.dto.PostResponseDTO;
import se.jensen.anton.springer.mapper.PostMapper;
import se.jensen.anton.springer.model.Post;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.PostRepository;
import se.jensen.anton.springer.repo.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
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

    public PostResponseDTO findById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            Post realPost = post.get();
            return postMapper.toDto(realPost);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
    }

    public List<PostResponseDTO> findAll() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toDto)
                .toList();
    }

    public void updatePost(Long id, PostRequestDTO dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        
        postMapper.updateEntity(dto, post);
        postMapper.toDto(post);
    }

    public PostResponseDTO addPost(Long userId, PostRequestDTO dto) {
        Post post = postMapper.fromDto(dto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id "
                        + userId));
        post.setUser(user);
        postRepository.save(post);
        return postMapper.toDto(post);
    }

    public void deletePost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.deleteById(id);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
    }
}
