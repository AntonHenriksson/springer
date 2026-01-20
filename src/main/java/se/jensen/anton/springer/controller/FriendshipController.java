package se.jensen.anton.springer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.FriendshipRespondDTO;
import se.jensen.anton.springer.service.FriendshipService;

import java.util.List;

@RestController
@RequestMapping("/friendship")
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    //hämta oavsett status
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<FriendshipRespondDTO>> get(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.getFriendshipsAnyStatus(userId));
    }

    //hämta accepterade
    @GetMapping("/users/{userId}/friends")
    public ResponseEntity<List<FriendshipRespondDTO>> getFriends(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.getFriendShipsAccepted(userId));
    }

    //aceptera vänskap
    @PutMapping("/{id}/accept")
    public ResponseEntity<FriendshipRespondDTO> accept(@PathVariable Long id) {
        return ResponseEntity.ok(friendshipService.acceptFriendship(id));
    }

    //rejecta vänskap
    @PutMapping("/{id}/reject")
    public ResponseEntity<FriendshipRespondDTO> reject(@PathVariable Long id) {
        return ResponseEntity.ok(friendshipService.rejectFriendship(id));
    }

}
