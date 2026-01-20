package se.jensen.anton.springer.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.dto.UserResponseDTO;
import se.jensen.anton.springer.mapper.UserMapper;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    //unit test som testar service logik och inget annat


    //om testet inte kastar exception ska det passa
    //verifierar bara om metoden har kallats
    @Test
    void deleteUserTest() {
        //arrange
        User user = new User();
        user.setId(0L);

        //stubba userRepository eftersom .orElseThrow kommer kasta
        //pga ingen jpa
        when(userRepository.findById(0L))
                .thenReturn(Optional.of(user));
        //act
        userService.deleteUser(0L);

        //assert
        verify(userRepository).deleteById(0L);
    }


    //testar service, ska pass med id null pga ingen jpa
    //stubbar passwordencoder för NPE
    @Test
    void addUserTest() {

        //arrange
        UserRequestDTO dto = new UserRequestDTO(
                "testUser",
                "user@mail.se",
                "password1",
                "USER",
                "test-user",
                "no bio",
                "no image"
        );
        //måste stubba passwordEncoder eftersom den är en mock = returnar null
        //add user kräver en .encode
        when(passwordEncoder.encode(anyString())).thenReturn("ENCODED");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        //act
        userService.addUser(dto);

        //assert
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("testUser", saved.getUsername());
    }

    //hämtar alla users
    @Test
    void getAllUsersTest() {

        //arrange
        User user = new User();
        user.setId(0L);
        when(userRepository.findAll()).thenReturn(java.util.List.of(user));
        //act
        List<UserResponseDTO> result = userService.getAllUser();
        //assert
        UserResponseDTO dto = result.get(0);
        verify(userRepository).findAll();
        assertEquals(1, result.size());
        assertEquals(0, dto.id());
    }


}

