package se.jensen.anton.springer.mapper;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.dto.UserRespondDTO;
import se.jensen.anton.springer.model.User;

@Component
public class UserMapper {

    public UserRespondDTO toDto(User user) {
        return new UserRespondDTO(user.getUsername());
    }

    public User fromDto(UserRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        user.setBio(dto.bio());
        user.setDisplayName(dto.displayName());
        user.setProfileImagePath(dto.profileImagePath());
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

    //hjälp metod till setters??
}
