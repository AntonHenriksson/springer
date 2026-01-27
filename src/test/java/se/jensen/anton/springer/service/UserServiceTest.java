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

/**
 * This test class tests the {@link UserService} class
 * It mocks the {@link UserRepository} and {@link PasswordEncoder}
 * The class uses @Spy to create a spy of the {@link UserMapper}
 */

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


    /**
     * Unittest for the deleteUser method
     * This test tries to delete a user with id 0
     * It uses a stubbed userRepository to return an Optional.of(user)
     * It asserts that the userRepository.findById method has been called
     * It verifies that userRepository.deleteById behaves as expected
     */

    @Test
    void deleteUserTest() {
        //arrange
        User user = new User();
        user.setId(0L);


        when(userRepository.findById(0L))
                .thenReturn(Optional.of(user));
        //act
        userService.deleteUser(0L);

        //assert
        verify(userRepository).findById(0L);
        verify(userRepository).deleteById(0L);
    }

    /**
     * Unittest for the addUser method
     * This test tries to add a user with username testUser
     * It uses a stubbed passwordEncoder to return "ENCODED"
     * It asserts that the userRepository.save-method has been called
     * A captor is used to capture the user that has been saved
     * The captured user is then asserted to have the username testUser
     */

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

        when(passwordEncoder.encode(anyString())).thenReturn("ENCODED");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        //act
        userService.addUser(dto);

        //assert
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("testUser", saved.getUsername());
    }

    /**
     * Unittest for the getAllUsers method
     * This test tries to get all users
     * It uses a stubbed userRepository to return a list of users
     * It asserts that the userRepository.findAll method has been called
     * It asserts that the returned list has size 1
     * It asserts that the first user in the list has id 0
     */
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
        assertEquals(0L, dto.id());
    }


}

