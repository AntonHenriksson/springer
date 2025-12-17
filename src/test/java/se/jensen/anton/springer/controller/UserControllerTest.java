package se.jensen.anton.springer.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.jensen.anton.springer.dto.UserRequestDTO;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.UserRepository;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        User user = new User();
        user.setRole("ADMIN");
        user.setPassword(passwordEncoder.encode("password1"));
        user.setEmail("mail@mail.se");
        user.setBio("my bio");
        user.setDisplayName("testAdmin");
        user.setUsername("Admin");
        userRepository.save(user);
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users")
                                .with(httpBasic("Admin", "password1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").exists());
    }


    @Test
    void shouldGetUserById() throws Exception {
        User savedUser = userRepository.findAll().getFirst();

        mockMvc.perform(
                        MockMvcRequestBuilders.get(
                                        "/users/" + savedUser.getId())
                                .with(httpBasic("Admin", "password1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.id").value(savedUser.getId()));
    }

    @Test
    void shouldAddUser() throws Exception {
        UserRequestDTO newUser = new UserRequestDTO
                ("User"
                        , "mailUser@mail.se"
                        , "password1"
                        , "USER"
                        , "userTest"
                        , "userBio"
                        , null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").exists());
    }


}
