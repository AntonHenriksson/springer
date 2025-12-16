package se.jensen.anton.springer.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.jensen.anton.springer.dto.PostResponseDTO;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.dto.UserResponseDTO;
import se.jensen.anton.springer.dto.UserWithPostsResponseDto;
import se.jensen.anton.springer.mapper.PostMapper;
import se.jensen.anton.springer.mapper.UserMapper;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper, PostMapper postMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.postMapper = postMapper;
    }

    public UserResponseDTO findUserById(Long id) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User realUser = user.get();
            return userMapper.toDto(realUser);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    public List<UserResponseDTO> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public void updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        userMapper.updateEntity(dto, user);
        userRepository.save(user);

        userMapper.toDto(user);
    }

    public UserResponseDTO addUser(UserRequestDTO dto) {
        User user = userMapper.fromDto(dto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    public UserWithPostsResponseDto getUserWithPosts(Long id) {
        User user = userRepository.findUserWithPosts(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        List<PostResponseDTO> posts = user.getPosts()
                .stream()
                .map(postMapper::toDto)
                .toList();
        UserResponseDTO dto = new UserResponseDTO(user.getUsername()
                , user.getEmail()
                , user.getRole());
        return new UserWithPostsResponseDto(dto, posts);
    }

    public UserResponseDTO getUserByName(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        UserResponseDTO dto = userMapper.toDto(user);
        return dto;
    }
}
