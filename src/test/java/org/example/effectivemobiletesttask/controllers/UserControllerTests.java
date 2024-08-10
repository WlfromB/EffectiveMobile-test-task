package org.example.effectivemobiletesttask.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.effectivemobiletesttask.dto.user.UserRequestCreate;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.pagination.PageableCreator;
import org.example.effectivemobiletesttask.pagination.PaginationParams;
import org.example.effectivemobiletesttask.services.auth.JwtProvider;
import org.example.effectivemobiletesttask.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.webjars.NotFoundException;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PageableCreator pageableCreator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createUserShouldReturnCreated() throws Exception {
        UserRequestCreate requestCreate = new UserRequestCreate();
        requestCreate.setEmail("olezhka@gmail.com");
        requestCreate.setPassword("best1password");
        requestCreate.setLogin("Olezhka");
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setPassword("best1password");
        savedUser.setEmail("olezhka@gmail.com");
        savedUser.setLogin("Olezhka");
        when(userService.createUser(requestCreate)).thenReturn(savedUser);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCreate)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/user/1"))
                .andExpect(content().string("successfully created user"));

        verify(userService).createUser(requestCreate);
    }

    @Test
    void createUserShouldReturnBadRequestForInvalidInput() throws Exception {
        UserRequestCreate requestCreate = new UserRequestCreate();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsersShouldReturnOk() throws Exception {
        User user = new User();
        user.setLogin("Olezhka");
        String token = jwtProvider.generateAccessToken(user);
        PaginationParams paginationParams = new PaginationParams();
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> usersPage = new PageImpl<>(List.of(new User()));

        when(pageableCreator.create(paginationParams)).thenReturn(pageable);
        when(userService.findAll(pageable)).thenReturn(usersPage);

        mockMvc.perform(get("/user/users")
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(userService).findAll(pageable);
    }

    @Test
    void getAllUsersShouldReturnForbiddenWhenUnauthorized() throws Exception {
        mockMvc.perform(get("/user/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserByIdShouldReturnOk() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setLogin("Olezhka");
        String token = jwtProvider.generateAccessToken(user);
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/user")
                        .param("id", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("Olezhka"));

        verify(userService).findById(1L);
    }

    @Test
    void getUserByIdShouldReturnNotFound() throws Exception {
        User user = new User();
        user.setLogin("Olezhka");
        String token = jwtProvider.generateAccessToken(user);
        when(userService.findById(1L)).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/user")
                        .param("id", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserByIdShouldReturnBadRequestForInvalidId() throws Exception {
        User user = new User();
        user.setLogin("Olezhka");
        String token = jwtProvider.generateAccessToken(user);
        mockMvc.perform(get("/user")
                        .param("id", "0")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserByIdShouldReturnForbiddenWhenUnauthorized() throws Exception {
        mockMvc.perform(get("/user")
                        .param("id", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserByLoginShouldReturnOk() throws Exception {
        User user = new User();
        user.setLogin("Olezhka");
        String token = jwtProvider.generateAccessToken(user);
        when(userService.findByLogin("Olezhka")).thenReturn(user);

        mockMvc.perform(get("/user/login")
                        .param("login", "Olezhka")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("Olezhka"));

        verify(userService).findByLogin("Olezhka");
    }

    @Test
    void getUserByLoginShouldReturnNotFound() throws Exception {
        User user = new User();
        user.setLogin("Olezhka");
        String token = jwtProvider.generateAccessToken(user);
        when(userService.findByLogin("Olezhka")).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/user/login")
                        .param("login", "Olezhka")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserByLoginShouldReturnForbiddenWhenUnauthorized() throws Exception {
        mockMvc.perform(get("/user/login")
                        .param("login", "Olezhka"))
                .andExpect(status().isForbidden());
    }
}

