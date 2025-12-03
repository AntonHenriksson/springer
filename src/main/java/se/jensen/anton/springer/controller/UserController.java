package se.jensen.anton.springer.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.dto.UserRespondDTO;
import se.jensen.anton.springer.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private List<User> users = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<UserRespondDTO>> getUsers() {
        List<UserRespondDTO> dto = users.stream().
                map(user -> new UserRespondDTO(
                        user.getUsername()
                )).toList();

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRespondDTO> getUser(@PathVariable int id) {
        if (id < 0 || id >= users.size() || users.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        UserRespondDTO dto = new UserRespondDTO(
                users.get(id).getUsername()
        );

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserRespondDTO> addUser(@RequestBody @Valid UserRequestDTO dtoUser) {
        boolean exists = users.stream().anyMatch(u -> u.getUsername().
                equalsIgnoreCase(dtoUser.username()));
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User user = new User();
        user.setUsername(dtoUser.username());
        user.setPassword(dtoUser.password());

        UserRespondDTO dto = new UserRespondDTO(user.getUsername());
        //tog bort password return för säkerhet
        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRespondDTO> updateUser(@PathVariable int id,
                                                     @RequestBody @Valid UserRequestDTO dto) {
        boolean exists = users.stream().anyMatch(u -> u.getUsername().
                equalsIgnoreCase(dto.username()) && users.indexOf(u) != id);
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (id < 0 || id >= users.size() || users.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        User user = users.get(id);
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        UserRespondDTO dto2 = new UserRespondDTO(dto.username());
        return ResponseEntity.ok(dto2);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        if (id < 0 || id >= users.size() || users.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        users.remove(id);
        return ResponseEntity.noContent().build();
    }

}
