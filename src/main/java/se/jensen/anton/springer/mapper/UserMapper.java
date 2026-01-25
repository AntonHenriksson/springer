package se.jensen.anton.springer.mapper;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.dto.UserResponseDTO;
import se.jensen.anton.springer.dto.UserUpdateRequestDTO;
import se.jensen.anton.springer.model.User;

/**
 * Mapper component for converting between User-related DTOs and {@link User} entity.
 * The mapper focuses on data transformation.
 */
@Component
public class UserMapper {

    /**
     * This method converts {@link User} entity to a {@link UserResponseDTO}
     *
     * @param user {@link User} entity instance to be converted
     * @return {@link UserResponseDTO} containing user info except password
     */
    public UserResponseDTO toDto(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getDisplayName(),
                user.getBio(),
                user.getProfileImagePath());

    }

    /**
     * This method converts {@link UserRequestDTO} to a {@link User} entity
     *
     * @param dto {@link UserRequestDTO} to be converted to a {@link User} entity
     * @return {@link User} entity to be stored in the database
     */
    public User fromDto(UserRequestDTO dto) {
        User user = new User();
        createEntity(dto, user);
        return user;
    }

    /**
     * This method converts a {@link UserRequestDTO} to a {@link User} entity
     * It is used when creating a new user.
     *
     * @param dto  {@link UserRequestDTO} containing given user info
     * @param user an empty {@link User} entity to which values are set
     */
    public void createEntity(UserRequestDTO dto, User user) {
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        user.setBio(dto.bio());
        user.setDisplayName(dto.displayName());
        user.setProfileImagePath(dto.profileImagePath());
    }

    /**
     * This method updates a {@link User} entity with values from {@link UserUpdateRequestDTO} values
     * It is used when updating an existing user.
     *
     * @param dto  {@link UserUpdateRequestDTO} containing updated user info
     * @param user the existing {@link User} entity to which updated values are set
     */
    public void updateEntity(UserUpdateRequestDTO dto, User user) {
        if (dto.username() != null && !dto.username().isBlank())
            user.setUsername(dto.username());
        if (dto.email() != null && !dto.email().isBlank())
            user.setEmail(dto.email());
        if (dto.bio() != null && !dto.bio().isBlank())
            user.setBio(dto.bio());
        if (dto.displayName() != null && !dto.displayName().isBlank())
            user.setDisplayName(dto.displayName());
        if (dto.profileImagePath() != null && !dto.profileImagePath().isBlank())
            user.setProfileImagePath(dto.profileImagePath());
    }


}
