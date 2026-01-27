package se.jensen.anton.springer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.FriendshipRespondDTO;
import se.jensen.anton.springer.service.FriendshipService;

import java.util.List;


/**
 * REST controller for managing friendships between users.
 */
@RestController
@RequestMapping("/friendship")
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    /**
     * GET-method to fetch all friendship requests of a user, regardless of their status.
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param userId ID of the user whose friendships are being fetched
     * @return {@link ResponseEntity} includes a {@link List} of {@link FriendshipRespondDTO} objects
     */
    //hämta oavsett status
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<FriendshipRespondDTO>> get(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.getFriendshipsAnyStatus(userId));
    }

    /**
     * GET-method to fetch only the accepted friendships of the user.
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param userId ID of the user whose accepted friendships are being fetched
     * @return {@link ResponseEntity} includes a {@link List} of {@link FriendshipRespondDTO} objects
     */
    //hämta accepterade
    @GetMapping("/users/{userId}/friends")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<FriendshipRespondDTO>> getFriends(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.getFriendShipsAccepted(userId));
    }

    /**
     * PUT-method to accept a pending friendship request.
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param id ID of the friendship request to accept
     * @return {@link ResponseEntity} includes the updated {@link FriendshipRespondDTO}
     */
    //acceptera vänskap
    @PutMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<FriendshipRespondDTO> accept(@PathVariable Long id) {
        return ResponseEntity.ok(friendshipService.acceptFriendship(id));
    }

    /**
     * PUT-method to decline a pending friendship request
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param id ID of the friendship request to reject
     * @return {@link ResponseEntity} includes the updated {@link FriendshipRespondDTO}
     */
    //rejecta vänskap
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<FriendshipRespondDTO> reject(@PathVariable Long id) {
        return ResponseEntity.ok(friendshipService.rejectFriendship(id));
    }

    /**
     * PUT-method to sends a friendship request from one user to another.
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param userId     ID of the user sending the friendship request
     * @param receiverId ID of the user receiving the friendship request
     * @return {@link ResponseEntity} includes the created {@link FriendshipRespondDTO}
     */
    //sök vänskap
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/users/{userId}/add-friend")
    public ResponseEntity<FriendshipRespondDTO> addFriend(@PathVariable Long userId, Long receiverId) {
        return ResponseEntity.ok(friendshipService.sendFriendRequest(userId, receiverId));
    }

}
