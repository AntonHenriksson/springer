package se.jensen.anton.springer.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.*;
import se.jensen.anton.springer.service.PostService;
import se.jensen.anton.springer.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PermitAll
    @PostMapping
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody @Valid UserRequestDTO dtoUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUser(dtoUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                      @RequestBody @Valid UserRequestDTO dto) {
        userService.updateUser(id, dto);
        return ResponseEntity.ok(userService.findUserById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{userId}/posts")
    public ResponseEntity<PostResponseDTO> post(@PathVariable Long userId, @RequestBody @Valid PostRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.addPost(userId, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}/with-posts")
    public ResponseEntity<UserWithPostsResponseDto> getByPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserWithPosts(userId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.getUserByName(username));
    }
}
