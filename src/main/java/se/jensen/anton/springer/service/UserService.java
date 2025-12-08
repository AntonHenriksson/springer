package se.jensen.anton.springer.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.dto.UserRespondDTO;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRespondDTO findUserById(long id) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User realUser = user.get();
            return toDto(realUser);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    public List<UserRespondDTO> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserRespondDTO(user.getUsername()))
                .toList();
    }

    public UserRespondDTO updateUser(long id, UserRequestDTO dto) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User realUser = user.get();
            realUser.setUsername(dto.username());
            realUser.setPassword(dto.password());
            realUser.setEmail(dto.email());
            realUser.setBio(dto.bio());
            realUser.setDisplayName(dto.displayName());
            realUser.setRole(dto.role());
            realUser.setProfileImagePath(dto.profileImagePath());
            userRepository.save(realUser);
            return toDto(realUser);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    public UserRespondDTO addUser(UserRequestDTO dto) {
        User user = fromDto(dto);
        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
        userRepository.save(user);

        return toDto(user);
    }

    public void deleteUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    private UserRespondDTO toDto(User user) {
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
