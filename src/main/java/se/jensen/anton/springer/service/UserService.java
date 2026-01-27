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

/**
 * Service layer for handling user-related business logic.
 * This class provides operations such as creating, fetching, updating, and deleting users, as well as managing passwords and fetching user-related data.
 * Access to methods is controlled using {@link PreAuthorize}.
 */
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

    /**
     * This method fetches a specific user by its ID
     * Access is allowed to users with either the ADMIN- or USER-role
     *
     * @param id ID of the user to fetch
     * @return {@link UserResponseDTO} mapped from the {@link User} entity
     * @throws ResponseStatusException with HTTP status 404 if no user with the given ID exists
     */
    //only admin, consider making a /public and publicResponseDTO for users
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
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

    /**
     * This method fetches all users stored in the database
     * Access is only allowed to users with the ADMIN-role
     *
     * @return {@link List} of {@link UserResponseDTO} mapped from the {@link User} entity
     */
    //only admin should see this
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> getAllUser() {
        logger.debug("Finding all users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * This method updates an existing user's info
     * Only authenticated users are only allowed to update their own account.
     *
     * @param id  ID of the user to update
     * @param dto {@link UserUpdateRequestDTO} containing data to update the user
     * @return updated user info as {@link UserResponseDTO}
     * @throws ResponseStatusException with HTTP status 404 if no user with the given ID exists
     */
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

    /**
     * This method updates a user's password
     * Only authenticated users are only allowed to update their own password.
     * The updated password is encoded using {@link PasswordEncoder} before it is saved.
     *
     * @param id  ID of the user to update the password
     * @param dto {@link UserPasswordRequestDTO} containing the new password
     * @throws ResponseStatusException with HTTP status 404 if no user with the given ID exists
     */
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

    /**
     * This method creates a new user.
     * The password is encoded using {@link PasswordEncoder} before it is saved.
     *
     * @param dto {@link UserRequestDTO} containing the new user's info including username, email, password, role, displayName, bio and profileImagePath
     * @return the newly created user as {@link UserResponseDTO}
     */
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

    /**
     * This method deletes a specific user identified by the given ID
     * Only authenticated users are only allowed to delete their own users.
     *
     * @param id ID of the user to delete
     * @throws ResponseStatusException with HTTP status 404 if no user with the given ID exists
     */
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

    /**
     * This method fetches a user along with their posts
     * Access is allowed to users with either the ADMIN- or USER-role.
     *
     * @param id ID of the user
     * @return {@link UserWithPostsResponseDTO} containing the user's data and their list of posts
     * @throws ResponseStatusException with HTTP status 404 if no user with the given ID exists
     */
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


    /**
     * This method fetches the currently authenticated user
     *
     * @return {@link UserResponseDTO} containing the authenticated user's data
     * @throws ResponseStatusException with HTTP status 401 if the authenticated user cannot be found.
     */
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
