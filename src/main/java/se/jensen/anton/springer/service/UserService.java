package se.jensen.anton.springer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, UserMapper userMapper, PostMapper postMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.passwordEncoder = passwordEncoder;
    }

    //only admin, consider making a /public and publicResponseDTO for users
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO findUserById(Long id) {
        logger.debug("Finding user with id={}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User realUser = user.get();
            logger.debug("Found user with id={}", id);
            return userMapper.toDto(realUser);
        }
        logger.debug("User not found with id={}", id);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    //only admin should see this
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> getAllUser() {
        logger.debug("Finding all users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    @PreAuthorize("@userAuth.checkIfAuth(#id)")
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO dto) {
        logger.debug("Updating user with id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.debug("Didnt find user with id={}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "User not found");
                });
        userMapper.updateEntity(dto, user);
        userRepository.save(user);
        logger.info("User updated, id={}", id);
        return userMapper.toDto(user);
    }

    @Transactional
    @PreAuthorize("@userAuth.checkIfAuth(#id)")
    public void updatePassword(Long id, UserPasswordRequestDTO dto) {
        logger.debug("Updating password for user with id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.debug("Didnt find user with id={}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "User not found");
                });
        user.setPassword(passwordEncoder.encode(dto.password()));
        logger.info("Password updated for user, id={}", id);

    }

    // måste för tillfället vara öppen till andra
    // hur fungerar /public???
    @Transactional
    public UserResponseDTO addUser(UserRequestDTO dto) {
        logger.debug("Adding new user");
        User user = userMapper.fromDto(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("User added, id={}", user.getId());
        return userMapper.toDto(user);
    }

    @Transactional
    @PreAuthorize("@userAuth.checkIfAuth(#id)")
    public void deleteUser(Long id) {
        logger.debug("Deleting user with id={}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            logger.info("User deleted, id={}", id);
            return;
        }
        logger.debug("User not found with id={}", id);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public UserWithPostsResponseDTO getUserWithPosts(Long id) {
        User user = userRepository.findUserWithPosts(id)
                .orElseThrow(() -> {
                    logger.debug("Didnt find user with id={}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "User not found");
                });
        List<PostResponseDTO> posts = user.getPosts()
                .stream()
                .map(postMapper::toDto)
                .toList();
        UserResponseDTO dto = userMapper.toDto(user);
        logger.debug("Found user with id={}", id);
        return new UserWithPostsResponseDTO(dto, posts);
    }


    @PreAuthorize("isAuthenticated()")
    public UserResponseDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("Fetching current user profile");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Authenticated user NOT found in DB");
                    return new ResponseStatusException(HttpStatus
                            .UNAUTHORIZED, "Invalid AUTH");
                });

        return userMapper.toDto(user);
    }

}
