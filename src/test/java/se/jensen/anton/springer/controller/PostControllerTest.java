package se.jensen.anton.springer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import se.jensen.anton.springer.model.Post;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.PostRepository;
import se.jensen.anton.springer.repo.UserRepository;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        postRepository.deleteAll();
        userRepository.deleteAll();


        User user = new User();
        user.setRole("ADMIN");
        user.setPassword(passwordEncoder.encode("password1"));
        user.setEmail("mail@mail.se");
        user.setBio("my bio");
        user.setDisplayName("testAdmin");
        user.setUsername("Admin");
        userRepository.save(user);

        for (int i = 0; i < 3; i++) {
            Post post = new Post();
            post.setUser(user);
            post.setText("post " + i);
            post.setCreated(LocalDateTime.now().minusMinutes(i)); // nyast först
            postRepository.save(post);
        }
    }

    @Test
    void shouldGetGlobalFeed() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/posts")
                                .with(httpBasic("Admin", "password1"))
                                .param("page", "0")
                                .param("size", "10")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].text").exists())
                .andExpect(jsonPath("$.content[1].text").exists())
                .andExpect(jsonPath("$.content[2].text").exists());
    }
}


