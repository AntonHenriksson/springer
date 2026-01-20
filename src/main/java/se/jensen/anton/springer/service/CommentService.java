package se.jensen.anton.springer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import se.jensen.anton.springer.dto.CommentRequestDTO;
import se.jensen.anton.springer.dto.CommentResponseDTO;
import se.jensen.anton.springer.mapper.CommentMapper;
import se.jensen.anton.springer.model.Comment;
import se.jensen.anton.springer.model.Post;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.CommentRepository;
import se.jensen.anton.springer.repo.PostRepository;
import se.jensen.anton.springer.repo.UserRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    public CommentResponseDTO createComment(Long postId, String username, CommentRequestDTO request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = new Comment();
        comment.setText(request.text());
        comment.setPost(post);
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toDto(savedComment);
    }

    public Page<CommentResponseDTO> getCommentsForPost(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "created"));

        return commentRepository
                .findByPostId(postId, pageable)
                .map(commentMapper::toDto);
    }
}
