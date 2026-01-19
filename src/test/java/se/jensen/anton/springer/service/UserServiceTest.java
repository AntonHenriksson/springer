package se.jensen.anton.springer.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.mapper.UserMapper;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapper userMapper = new UserMapper();

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    UserService userService;


    //testar service ska pass med id = 0
    @Test
    void deleteUserTest() {
        Long id = 0L;
        User user = new User();
        user.setId(id);
        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        userService.deleteUser(id);
        verify(userRepository).deleteById(id);
    }


    //testar service, ska pass med id null pga ingen jpa
    //stubbar passwordencoder för NPE
    @Test
    void addUserTest() {
        UserRequestDTO dto = new UserRequestDTO(
                "testUser",
                "user@mail.se",
                "password1",
                "USER",
                "test-user",
                "no bio",
                "no image"
        );


        when(passwordEncoder.encode(anyString())).thenReturn("ENCODED");
        userService.addUser(dto);

        verify(userRepository).save(any(User.class));
    }
}

