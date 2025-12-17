package se.jensen.anton.springer.mapper;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.dto.UserResponseDTO;
import se.jensen.anton.springer.model.User;

@Component
public class UserMapper {


    public UserResponseDTO toDto(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());

        //lägga till och skapa ny dto constr när jag vet var vi är påväg i programmet
    }

    public User fromDto(UserRequestDTO dto) {
        User user = new User();
        updateEntity(dto, user);
        return user;
    }

    public void updateEntity(UserRequestDTO dto, User user) {
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        user.setBio(dto.bio());
        user.setDisplayName(dto.displayName());
        user.setProfileImagePath(dto.profileImagePath());
    }


}
