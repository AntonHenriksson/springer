package se.jensen.anton.springer.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.PostRequestDTO;
import se.jensen.anton.springer.dto.PostRespondDTO;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.dto.UserRespondDTO;
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

    @GetMapping
    public ResponseEntity<List<UserRespondDTO>> getUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRespondDTO> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserRespondDTO> addUser(@RequestBody @Valid UserRequestDTO dtoUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUser(dtoUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRespondDTO> updateUser(@PathVariable long id,
                                                     @RequestBody @Valid UserRequestDTO dto) {
        userService.updateUser(id, dto);
        return ResponseEntity.ok(userService.findUserById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{userId}/posts")
    public ResponseEntity<PostRespondDTO> post(@PathVariable Long userId, @RequestBody @Valid PostRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.addPost(userId, dto));
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<PostRespondDTO> getById(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.findById(userId));
    }

}
