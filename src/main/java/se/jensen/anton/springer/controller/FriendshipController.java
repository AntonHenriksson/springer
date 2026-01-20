package se.jensen.anton.springer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.FriendshipRespondDTO;

@RestController
@RequestMapping("/friendship")
public class FriendshipController {

    @GetMapping("/{id}")
    public ResponseEntity<FriendshipRespondDTO> get(@RequestParam Long id) {
        return ResponseEntity.ok(null); //service här med lista på friendships
    }

    @GetMapping("/users/{id}/friends")
    public ResponseEntity<FriendshipRespondDTO> getFriends(@PathVariable Long id) {
        return ResponseEntity.ok(null); // service här med /user/id/friends
    }

    @PutMapping("/{id}")
    public ResponseEntity<FriendshipRespondDTO> accept(@PathVariable Long id) {
        return ResponseEntity.ok(null); //service här med status update
    }

    @PutMapping("/{id}")
    public ResponseEntity<FriendshipRespondDTO> reject(@PathVariable Long id) {
        return ResponseEntity.ok(null); //service här med status reject
    }

}
