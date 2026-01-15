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

    // Endpoint to get all users (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    // Endpoint to get a single user (All get access)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    // Endpoint to let everyone create a user
    @PermitAll
    @PostMapping
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody @Valid UserRequestDTO dtoUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUser(dtoUser));
    }

    // Endpoint to update a user
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                      @RequestBody @Valid UserUpdateRequestDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    // Endpoint to update password
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updatePassword(@PathVariable Long id,
                                                          @RequestBody @Valid UserPasswordRequestDTO dto) {
        userService.updatePassword(id, dto);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to delete a user
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to create a post as a user
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDTO> post(@RequestBody @Valid PostRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.addPost(dto));
    }

    // Endpoint to get post by users id
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{userId}/with-posts")
    public ResponseEntity<UserWithPostsResponseDTO> getByPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserWithPosts(userId));
    }

    // Endpoint to show the user who is logged in (yourself)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.getUserByName(username));
    }
}
