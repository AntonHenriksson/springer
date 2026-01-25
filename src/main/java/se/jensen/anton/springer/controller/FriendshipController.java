package se.jensen.anton.springer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<FriendshipRespondDTO>> get(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.getFriendshipsAnyStatus(userId));
    }

    //hämta accepterade
    @GetMapping("/users/{userId}/friends")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<FriendshipRespondDTO>> getFriends(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.getFriendShipsAccepted(userId));
    }

    //acceptera vänskap
    @PutMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<FriendshipRespondDTO> accept(@PathVariable Long id) {
        return ResponseEntity.ok(friendshipService.acceptFriendship(id));
    }

    //rejecta vänskap
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<FriendshipRespondDTO> reject(@PathVariable Long id) {
        return ResponseEntity.ok(friendshipService.rejectFriendship(id));
    }

    //sök vänskap
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/users/{userId}/add-friend")
    public ResponseEntity<FriendshipRespondDTO> addFriend(@PathVariable Long userId, Long receiverId) {
        return ResponseEntity.ok(friendshipService.sendFriendRequest(userId, receiverId));
    }

}
