package se.jensen.anton.springer.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.*;
import se.jensen.anton.springer.service.PostService;
import se.jensen.anton.springer.service.UserService;

import java.util.List;

/**
 * REST controller for managing users.
 * This class provides endpoints to create, read, update, and delete users as well as endpoints related to user posts and the currently authenticated user.
 * Access to the endpoints is restricted based on the user role (ADMIN, USER or everyone).
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    /**
     *
     * GET-method to fetch all users stored in the database, and return them as a list of UserResponseDTO objects
     * Access is only allower to users with the ADMIN-role
     *
     * @return　ResponseEntity which contains a list of UserResponseDTO objects
     */
    // Endpoint to get all users (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    /**
     * GET-method to fetch a specific user by its ID
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param id ID of the user you want to fetch
     * @return ResponseEntity containing a UserResponseDTO object of a specific user
     */
    // Endpoint to get a single user (All get access)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    /**
     * POST-method to create a new user
     * Requires user information based on UserRequestDTO
     * Access is allowed to all users
     *
     * @param dtoUser User info which consists of username, email, password, role, displayName, bio and profileImagePath
     * @return ResponseEntity containing the created UserResponseDTO object
     */
    // Endpoint to let everyone create a user
    @PermitAll
    @PostMapping
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody @Valid UserRequestDTO dtoUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUser(dtoUser));
    }

    /**
     * PATCH-method to update a specific user's information
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param id  ID of the user you want to update
     * @param dto UserUpdateRequestDCreate, Read, Update, Delete usersTO consists of username, email, displayName, bio and profileImagePath. Only the fields you want to change need to be provided.
     * @return ResponseEntity containing the updated UserResponseDTO object
     */
    // Endpoint to update a user
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                      @RequestBody @Valid UserUpdateRequestDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    /**
     * PUT-method to update the only user's password
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param id  ID for the user whose password you want to change
     * @param dto UserPasswordRequestDTO which only contains a password property
     * @return ResponseEntity containing the updated UserResponseDTO object
     */
    // Endpoint to update password
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updatePassword(@PathVariable Long id,
                                                          @RequestBody @Valid UserPasswordRequestDTO dto) {
        userService.updatePassword(id, dto);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE-method to delete a specific user by its ID
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param id ID of the user you want to delete
     * @return ResponseEntity with no content if the user has been successfully deleted
     */
    // Endpoint to delete a user
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * POST-method to create a new post
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param dto PostRequestDTO, which consists of text and creation timestamp, used to create a new post
     * @return ResponseEntity containing the created PostResponseDTO object
     */
    // Endpoint to create a post as a user
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDTO> post(@RequestBody @Valid PostRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.addPost(dto));
    }

    /**
     * GET-method to fetch a specific user together with all posts the user has created
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param userId the user's ID to fetch its posts
     * @return ResponseEntity containing a UserWithPostsResponseDTO object (the user's info and its posts)
     */
    // Endpoint to get post by users id
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{userId}/with-posts")
    public ResponseEntity<UserWithPostsResponseDTO> getByPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserWithPosts(userId));
    }

    /**
     * GET-method to
     *
     * @return ResponseEntity containing a UserResponseDTO object which contains the loggedin user's info
     */
    // Endpoint to show the user who is logged in (yourself)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

}
