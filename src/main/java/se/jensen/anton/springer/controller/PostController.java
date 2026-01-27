package se.jensen.anton.springer.controller;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.FeedResponseDTO;
import se.jensen.anton.springer.dto.PostRequestDTO;
import se.jensen.anton.springer.dto.PostResponseDTO;
import se.jensen.anton.springer.service.PostService;

import java.security.Principal;
import java.util.Optional;


/**
 * REST controller for managing posts.
 * This class provides endpoints to create, read, update, and delete posts.
 * All endpoints are available for users with either ADMIN- or USER-role.
 */
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * GET-method to fetch posts created by all users
     * The response is divided into pages
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param page Page index (default is 0)
     * @param size The number of posts per page (default size is 10 posts)
     * @return {@link ResponseEntity} containing a paginated list of {@link FeedResponseDTO}
     */
    // Returnerar det globala flödet av inlägg för alla användare, med sidindelning
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/get")
    public ResponseEntity<Page<FeedResponseDTO>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Optional<Long> userId
    ) {
        Page<FeedResponseDTO> feed = postService.getPosts(page, size, userId);
        return ResponseEntity.ok(feed);
    }

    /**
     * GET-method to fetch all posts created by the logged in user
     * The response is divided into pages
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param page      Page index (default is 0)
     * @param size      The number of posts per page (default size is 10 posts)
     * @param principal The security principal containing the authenticated user's info
     * @return {@link ResponseEntity} containing a paginated list of {@link FeedResponseDTO} created by the user
     */
    // Returnerar det personliga flödet för den inloggade användaren, med sidindelning
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/me")
    public ResponseEntity<Page<FeedResponseDTO>> getMyWall(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal
    ) {
        Page<FeedResponseDTO> wall = postService.getMyWall(principal.getName(), page, size);
        return ResponseEntity.ok(wall);
    }


    /**
     * GET-method to get one specific post using its ID
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param id ID of the specific post
     * @return {@link ResponseEntity} containing a {@link PostResponseDTO} for the post
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPosts(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    /**
     * PUT-method to update an existing post with the provided content
     * The timestamp of the post will be updated automatically.
     * Access is allowed to users with either the ADMIN- or USER-role.
     *
     * @param id  ID of the post to update
     * @param dto a {@link PostResponseDTO} containing the updated post's content
     * @return {@link ResponseEntity} containing a {@link PostResponseDTO} of the updated post
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable Long id, @RequestBody @Valid PostRequestDTO dto) {
        return ResponseEntity.ok(postService.updatePost(id, dto));
    }

    /**
     * DELETE-method to delete a specific post identified by its ID from the database
     * Access is allowed to users with either the ADMIN- or USER-role.
     *
     * @param id ID of the post to delete
     * @return an empty {@link ResponseEntity} with HTTP 204 status
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
