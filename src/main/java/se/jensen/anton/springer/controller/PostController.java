package se.jensen.anton.springer.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.PostRequestDTO;
import se.jensen.anton.springer.dto.PostRespondDTO;
import se.jensen.anton.springer.mapper.PostMapper;
import se.jensen.anton.springer.model.Post;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostMapper postMapper;
    List<Post> posts = new ArrayList<>();

    public PostController(PostMapper postmapper) {
        this.postMapper = postmapper;
    }

    @GetMapping
    public ResponseEntity<List<PostRespondDTO>> getAll() {

        List<PostRespondDTO> response = posts.stream().
                map(postMapper::toPostRespondDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostRespondDTO> get(@PathVariable int id) {
        if (id < 0 || id >= posts.size() || posts.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(postMapper.toPostRespondDTO(posts.get(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostRespondDTO> put(@PathVariable int id, @RequestBody @Valid PostRequestDTO dto) {
        if (id < 0 || id >= posts.size() || posts.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Post post = posts.get(id);
        post.setText(dto.text());
        post.setCreated(dto.created());

        return ResponseEntity.ok(postMapper.toPostRespondDTO(post));
    }

    @PostMapping
    public ResponseEntity<PostRespondDTO> post(@RequestBody @Valid PostRequestDTO dto) {

        Post post = postMapper.toPost(dto);

        posts.add(post);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postMapper.toPostRespondDTO(post));
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
