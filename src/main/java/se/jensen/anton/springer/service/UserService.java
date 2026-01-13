package se.jensen.anton.springer.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import se.jensen.anton.springer.dto.*;
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
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PostMapper postMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.passwordEncoder = passwordEncoder;
    }

    //only admin, consider making a /public and publicResponseDTO for users
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO findUserById(Long id) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User realUser = user.get();
            return userMapper.toDto(realUser);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    //only admin should see this
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    @PreAuthorize("@userAuth.checkIfAuth(#id)")
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        userMapper.updateEntity(dto, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Transactional
    @PreAuthorize("@userAuth.checkIfAuth(#id)")
    public void updatePassword(Long id, UserPasswordRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setPassword(passwordEncoder.encode(dto.password()));
    }

    //todo måste för tillfället vara öppen till andra
    //todo hur fungerar /public???
    @Transactional
    public UserResponseDTO addUser(UserRequestDTO dto) {
        User user = userMapper.fromDto(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional
    @PreAuthorize("@userAuth.checkIfAuth(#id)")
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    @PreAuthorize("@userAuth.checkIfAuth(#id)")
    public UserWithPostsResponseDTO getUserWithPosts(Long id) {
        User user = userRepository.findUserWithPosts(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        List<PostResponseDTO> posts = user.getPosts()
                .stream()
                .map(postMapper::toDto)
                .toList();
        UserResponseDTO dto = userMapper.toDto(user);
        return new UserWithPostsResponseDTO(dto, posts);
    }

    //consider publicResponseDto
    //consider making a /public class for this instead
    //right now this is a /me in controller
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public UserResponseDTO getUserByName(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        UserResponseDTO dto = userMapper.toDto(user);
        return dto;
    }
}
