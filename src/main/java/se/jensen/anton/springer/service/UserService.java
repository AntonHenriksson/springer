package se.jensen.anton.springer.service;

import org.springframework.stereotype.Service;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.dto.UserRespondDTO;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserRespondDTO> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserRespondDTO(user.getUsername()))
                .toList();
    }

    public UserRespondDTO addUser(UserRequestDTO dto) {
        User user = fromDto(dto);
        return new UserRespondDTO(user.getUsername());
    }

    private User fromDto(UserRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        user.setBio(dto.bio());
        user.setDisplayName(dto.displayName());
        return user;
    }
}
