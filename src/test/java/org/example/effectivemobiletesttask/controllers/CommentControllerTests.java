package org.example.effectivemobiletesttask.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.effectivemobiletesttask.dto.comment.CommentCreateRequest;
import org.example.effectivemobiletesttask.entities.Comment;
import org.example.effectivemobiletesttask.entities.Row;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.services.auth.JwtProvider;
import org.example.effectivemobiletesttask.services.comment.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createComment_ShouldReturnCreated_WhenRequestIsValid() throws Exception {
        Row row = new Row();
        row.setTitle("Title");
        
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Valid Comment");
        comment.setRow(row);
        
        CommentCreateRequest request = new CommentCreateRequest();
        request.setRowTitle("Valid Title");
        request.setComment("Valid Comment");

        String token = jwtProvider.generateAccessToken(new User());
        
        when(commentService.createComment(any(CommentCreateRequest.class))).thenReturn(comment);

        mockMvc.perform(MockMvcRequestBuilders.post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void createComment_ShouldReturnBadRequest_WhenRequestIsInvalid() throws Exception {
        CommentCreateRequest request = new CommentCreateRequest();
        request.setRowTitle("");
        request.setComment("");

        String token = jwtProvider.generateAccessToken(new User());

        mockMvc.perform(MockMvcRequestBuilders.post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createComment_ShouldReturnForbidden_WhenNoAuthTokenProvided() throws Exception {
        CommentCreateRequest request = new CommentCreateRequest();
        request.setRowTitle("Valid Title");
        request.setComment("Valid Comment");

        mockMvc.perform(MockMvcRequestBuilders.post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
