package se.jensen.anton.springer.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.model.Post;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/posts")
public class PostController {

    List<Post> posts = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Post>> getAll() {
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> get(@PathVariable int id) {
        if (id < 0 || id >= posts.size() || posts.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(posts.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> put(@PathVariable int id, @RequestBody Post post) {
        if (id < 0 || id >= posts.size() || posts.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        posts.set(id, post);
        return ResponseEntity.ok(posts.get(id));
    }

    @PostMapping
    public ResponseEntity<Post> post(@RequestBody Post post) {
        if (post == null) {
            return ResponseEntity.badRequest().build();
        }
        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id < 0 || id >= posts.size() || posts.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        posts.remove(id);
        return ResponseEntity.noContent().build();
    }


}
