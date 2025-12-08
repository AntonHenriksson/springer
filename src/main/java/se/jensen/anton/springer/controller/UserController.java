package se.jensen.anton.springer.controller;

import jakarta.servlet.ServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.dto.UserRespondDTO;
import se.jensen.anton.springer.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<UserRespondDTO> addUser(@RequestBody @Valid UserRequestDTO dtoUser,
                                                  ServletRequest servletRequest) {
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

}
