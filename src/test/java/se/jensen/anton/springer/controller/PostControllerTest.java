package se.jensen.anton.springer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.jensen.anton.springer.dto.FeedResponseDTO;
import se.jensen.anton.springer.service.PostService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postsService;

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    void shouldGetPosts() throws Exception {

        FeedResponseDTO dto1 = new FeedResponseDTO(
                1L,
                "post 1",
                LocalDateTime.now(),
                10L,
                "testUser",
                "testAdmin"
        );
        FeedResponseDTO dto2 = new FeedResponseDTO(
                2L,
                "post 2",
                LocalDateTime.now(),
                20L,
                "testuser2",
                "testAdmin2"
        );
        FeedResponseDTO dto3 = new FeedResponseDTO(
                3L,
                "post 3",
                LocalDateTime.now(),
                30L,
                "testUser3",
                "testAdmin3"
        );

        Page<FeedResponseDTO> page = new PageImpl<>(List.of(dto1, dto2, dto3));

        when(postsService.getPosts(0, 10, Optional.empty())).thenReturn(page);

        mockMvc.perform(get("/posts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].text").value("post 1"))
                .andExpect(jsonPath("$.content[1].text").value("post 2"))
                .andExpect(jsonPath("$.content[2].text").value("post 3"));
    }
}




