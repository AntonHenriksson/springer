package se.jensen.anton.springer.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.PostRequestDTO;
import se.jensen.anton.springer.dto.PostRespondDTO;
import se.jensen.anton.springer.model.Post;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/posts")
public class PostController {

    List<Post> posts = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<PostRespondDTO>> getAll() {

        List<PostRespondDTO> response = posts.stream().
                map(post -> new PostRespondDTO(
                        post.getText(),
                        post.getCreated(),
                        post.getId())).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostRespondDTO> get(@PathVariable int id) {
        if (id < 0 || id >= posts.size() || posts.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        PostRespondDTO response = new PostRespondDTO(
                posts.get(id).getText(),
                posts.get(id).getCreated(),
                posts.get(id).getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostRespondDTO> put(@PathVariable int id, @RequestBody @Valid PostRequestDTO dto) {
        if (id < 0 || id >= posts.size() || posts.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Post post = posts.get(id);
        post.setText(dto.text());
        post.setCreated(dto.created());

        PostRespondDTO response = new PostRespondDTO(post.getText(),
                post.getCreated(), post.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PostRespondDTO> post(@RequestBody @Valid PostRequestDTO dto) {

        Post post = new Post();
        post.setText(dto.text());
        post.setCreated(dto.created());
        post.setId(0L);

        posts.add(post);
        PostRespondDTO response = new PostRespondDTO(post.getText(),
                post.getCreated(), post.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
