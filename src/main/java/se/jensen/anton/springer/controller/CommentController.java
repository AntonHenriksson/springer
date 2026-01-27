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

/**
 * REST controller for managing comments.
 * This class provides endpoints to create a comment and get all comments for a specific post.
 * All endpoints are available for users with either ADMIN- or USER-role.
 */
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * POST-method to create a comment for a specific post
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param postId    ID of the post to which the comment will be added
     * @param request   {@link CommentRequestDTO} object
     * @param principal The security principal which contains the authenticated user's info
     * @return {@link ResponseEntity} containing a created {@link CommentResponseDTO} and HTTP status 201
     */
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

    /**
     * GET-method to fetch all comments created for a specific post identified by its ID
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param postId ID of the specific post to which comments have been added
     * @param page   Page index (default is 0))
     * @param size   The number of posts per page (default size is 10 posts)
     * @return {@link ResponseEntity} containing a paginated list of {@link CommentResponseDTO} objects an HTTP status 200
     */
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
