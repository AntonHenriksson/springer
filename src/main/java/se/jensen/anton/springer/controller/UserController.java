package se.jensen.anton.springer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private List<String> users = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<String>> getUsers() {
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable int id) {
        if (id < 0 || id >= users.size() || users.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users.get(id));
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody String user) {
        if (user == null || users.contains(user)) {
            return ResponseEntity.badRequest().build();
        }
        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody String user) {
        if (user == null || user.isBlank()) {
            return ResponseEntity.badRequest().build();
        } else if (id < 0 || id >= users.size() || users.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        users.set(id, user);
        return ResponseEntity.ok(users.get(id));
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
