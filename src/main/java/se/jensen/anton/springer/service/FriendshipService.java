package se.jensen.anton.springer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import se.jensen.anton.springer.dto.FriendshipRespondDTO;
import se.jensen.anton.springer.mapper.FriendMapper;
import se.jensen.anton.springer.model.Friendship;
import se.jensen.anton.springer.repo.FriendshipRepository;
import se.jensen.anton.springer.repo.UserRepository;

import java.util.List;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final FriendMapper friendMapper;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(FriendshipService.class);

    public FriendshipService(FriendshipRepository friendshipRepository,
                             FriendMapper friendMapper,
                             UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.friendMapper = friendMapper;
        this.userRepository = userRepository;
    }

    //hämta alla accepterade vänskaper
    //här borde alla inloggade kunna se dina vänner?
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<FriendshipRespondDTO> getFriendShipsAccepted(Long userId) {
        logger.debug("Fetching ACCEPTED friendships for userId={}", userId);

        List<FriendshipRespondDTO> result = friendshipRepository
                .findBySenderIdOrReceiverId(userId, userId)
                .stream()
                .map(friendMapper::toDto)
                .filter(friendshipRespondDTO
                        -> friendshipRespondDTO.status()
                        == Friendship.Status.ACCEPTED)
                .toList();
        logger.info("Found {} accepted friendships for userId={}",
                result.size(), userId);

        return result;
    }

    //hämta alla vänskaper oavsett status
    //måste vara inloggad som receiver
    @PreAuthorize("@userAuth.checkIfAuth(#userId)")
    public List<FriendshipRespondDTO> getFriendshipsAnyStatus(Long userId) {
        logger.debug("Fetching ALL friendships for userId={}", userId);

        List<FriendshipRespondDTO> result = friendshipRepository
                .findBySenderIdOrReceiverId(userId, userId)
                .stream()
                .map(friendMapper::toDto)
                .toList();


        logger.info("Found {} total friendships for userId={}",
                result.size(), userId);
        return result;
    }

    //acceptera vänskap
    //måste vara inloggad som receiver auth fixa
    @PreAuthorize("@userAuth.checkIfAuth(#id)")
    public FriendshipRespondDTO acceptFriendship(Long id) {
        logger.info("Attempting to ACCEPT friendshipId={}", id);

        Friendship friendship = friendshipRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Friendship not found when accepting, friendshipId={}", id);
                    return new RuntimeException("Friendship not found");
                });
        logger.debug("FriendshipId={} status changing from {} → ACCEPTED",
                id, friendship.getStatus());

        friendship.setStatus(Friendship.Status.ACCEPTED);

        logger.info("FriendshipId={} successfully ACCEPTED", id);

        return friendMapper.toDto(friendshipRepository.save(friendship));
    }

    //rejecta vänskap
    //tänk här om man ska vara inloggad som receiver
    @PreAuthorize("@userAuth.checkIfAuth(#id)")
    public FriendshipRespondDTO rejectFriendship(Long id) {
        logger.info("Attempting to DECLINE friendshipId={}", id);

        Friendship friendship = friendshipRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Friendship not found when declining, friendshipId={}", id);
                    return new RuntimeException("Friendship not found");
                });
        logger.debug("FriendshipId={} status changing from {} → DECLINED",
                id, friendship.getStatus());
        friendship.setStatus(Friendship.Status.DECLINED);
        logger.info("FriendshipId={} successfully DECLINED", id);
        return friendMapper.toDto(friendshipRepository.save(friendship));
    }

    //skicka vänskapsförfrågan
    @PreAuthorize("@userAuth.checkIfAuth(#senderId)")
    public FriendshipRespondDTO sendFriendRequest(Long senderId, Long receiverId) {
        logger.info("User {} is sending friend request to user {}", senderId, receiverId);

        Friendship friendship = new Friendship();
        friendship.setSender(userRepository.findById(senderId).orElseThrow());
        friendship.setReceiver(userRepository.findById(receiverId).orElseThrow());
        friendship.setStatus(Friendship.Status.PENDING);

        Friendship saved = friendshipRepository.save(friendship);

        logger.info("Friend request created: friendshipId={}, senderId={}, receiverId={}",
                saved.getId(), senderId, receiverId);

        return friendMapper.toDto(saved);
    }

}
