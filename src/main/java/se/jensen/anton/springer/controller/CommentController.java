package se.jensen.anton.springer.controller;

import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.CommentRequestDTO;
import se.jensen.anton.springer.dto.CommentResponseDTO;
import se.jensen.anton.springer.service.CommentService;

import java.security.Principal;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(
            @RequestParam Long postId,
            @Valid @NonNull @RequestBody CommentRequestDTO request,
            @NonNull Principal principal) {

        String username = principal.getName();

        CommentResponseDTO response = commentService.createComment(postId, username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/post/{postId}")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsForPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CommentResponseDTO> response = commentService.getCommentsForPost(postId, page, size);
        return ResponseEntity.ok(response);
    }

}
