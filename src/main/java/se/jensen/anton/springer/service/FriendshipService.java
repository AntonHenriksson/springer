package se.jensen.anton.springer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.jensen.anton.springer.dto.FriendshipRespondDTO;
import se.jensen.anton.springer.mapper.FriendMapper;
import se.jensen.anton.springer.model.Friendship;
import se.jensen.anton.springer.repo.FriendshipRepository;

import java.util.List;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final FriendMapper friendMapper;
    private static final Logger logger = LoggerFactory.getLogger(FriendshipService.class);


    public FriendshipService(FriendshipRepository friendshipRepository, FriendMapper friendMapper) {
        this.friendshipRepository = friendshipRepository;
        this.friendMapper = friendMapper;
    }

    //hämta alla accepterade vänskaper
    public List<FriendshipRespondDTO> getFriendShipsAccepted(Long userId) {
        return friendshipRepository
                .findBySenderIdOrReceiverId(userId, userId)
                .stream()
                .map(friendMapper::toDto)
                .filter(friendshipRespondDTO
                        -> friendshipRespondDTO.status()
                        == Friendship.Status.ACCEPTED)
                .toList();
    }

    //hämta alla vänskaper oavsett status
    public List<FriendshipRespondDTO> getFriendshipsAnyStatus(Long userId) {
        return friendshipRepository
                .findBySenderIdOrReceiverId(userId, userId)
                .stream()
                .map(friendMapper::toDto)
                .toList();
    }

    //acceptera vänskap
    public FriendshipRespondDTO acceptFriendship(Long id) {
        Friendship friendship = friendshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Friendship not found"));
        friendship.setStatus(Friendship.Status.ACCEPTED);
        return friendMapper.toDto(friendshipRepository.save(friendship));
    }

    //rejecta vänskap
    public FriendshipRespondDTO rejectFriendship(Long id) {
        Friendship friendship = friendshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Friendship not found"));
        friendship.setStatus(Friendship.Status.DECLINED);
        return friendMapper.toDto(friendshipRepository.save(friendship));
    }
}
