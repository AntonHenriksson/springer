package se.jensen.anton.springer.mapper;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.dto.UserResponseDTO;
import se.jensen.anton.springer.dto.UserUpdateRequestDTO;
import se.jensen.anton.springer.model.User;

@Component
public class UserMapper {


    public UserResponseDTO toDto(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());

        //lägga till och skapa ny dto constr när jag vet var vi är påväg i programmet
    }

    public User fromDto(UserRequestDTO dto) {
        User user = new User();
        createEntity(dto, user);
        return user;
    }

    public void createEntity(UserRequestDTO dto, User user) {
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        user.setBio(dto.bio());
        user.setDisplayName(dto.displayName());
        user.setProfileImagePath(dto.profileImagePath());
    }

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
