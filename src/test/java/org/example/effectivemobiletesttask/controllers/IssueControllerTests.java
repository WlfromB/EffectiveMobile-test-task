package org.example.effectivemobiletesttask.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.effectivemobiletesttask.dto.issue.IssueChangeRequest;
import org.example.effectivemobiletesttask.dto.issue.IssueChangeStatusRequest;
import org.example.effectivemobiletesttask.dto.issue.IssueCreateRequest;
import org.example.effectivemobiletesttask.dto.issue.IssueDeleteRequest;
import org.example.effectivemobiletesttask.entities.Priority;
import org.example.effectivemobiletesttask.entities.Issue;
import org.example.effectivemobiletesttask.entities.Status;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.pagination.PageableCreator;
import org.example.effectivemobiletesttask.pagination.PaginationParams;
import org.example.effectivemobiletesttask.services.auth.JwtProvider;
import org.example.effectivemobiletesttask.services.row.IssueService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IssueControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssueService issueService;

    @MockBean
    private PageableCreator pageableCreator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;


    @Test
    void addRowShouldReturnCreated() throws Exception {
        IssueCreateRequest requestCreate = new IssueCreateRequest();
        requestCreate.setTitle("Название задачи.");
        requestCreate.setDescription("Описание задачи.");
        requestCreate.setPriority("HIGH");
        requestCreate.setSupplierLogin("Olezhka");
        requestCreate.setSupplierLogin("Olezhka");
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setPassword("best1password");
        savedUser.setEmail("olezhka@gmail.com");
        savedUser.setLogin("Olezhka");
        User savedSup = new User();
        savedSup.setId(2L);
        savedSup.setPassword("best1password");
        savedSup.setEmail("olezhka@gmail.com");
        savedSup.setLogin("Olezhka");
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setAuthor(savedUser);
        issue.setSupplier(savedSup);
        issue.setTitle("Название задачи.");
        issue.setDescription("Описание задачи.");
        issue.setPriority(Priority.valueOf("HIGH"));
        issue.setStatus(Status.valueOf("WAIT"));
        when(issueService.createIssue(requestCreate)).thenReturn(issue);

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(post("/row")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCreate))
                        .header("Authorization", token))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/row/1"))
                .andExpect(jsonPath("$.title").value(issue.getTitle()));

        verify(issueService).createIssue(requestCreate);
    }

    @Test
    void addRowShouldReturnBadRequestForInvalidInput() throws Exception {
        IssueCreateRequest requestCreate = new IssueCreateRequest();

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(post("/row")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCreate))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addRowShouldReturnBadRequestForWithoutAuthorization() throws Exception {
        mockMvc.perform(post("/row"))
                .andExpect(status().isForbidden());
    }

    @Test
    void changeStatusShouldReturnOk() throws Exception {
        IssueChangeStatusRequest request = new IssueChangeStatusRequest();
        request.setTitle("Название задачи.");
        request.setStatus("WAIT");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setPassword("best1password");
        savedUser.setEmail("olezhka@gmail.com");
        savedUser.setLogin("Olezhka");
        User suppl = new User();
        suppl.setId(1L);
        suppl.setPassword("best1password");
        suppl.setEmail("olezhka@gmail.com");
        suppl.setLogin("Olezhka");

        Issue issue = new Issue();
        issue.setId(1L);
        issue.setAuthor(savedUser);
        issue.setSupplier(suppl);
        issue.setPriority(Priority.valueOf("HIGH"));
        issue.setStatus(Status.valueOf("WAIT"));

        when(issueService.changeStatus(request)).thenReturn(issue);

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(patch("/row/change-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(issue.getTitle()));

        verify(issueService).changeStatus(request);
    }

    @Test
    void changeStatusShouldReturnBadRequestForInvalidInput() throws Exception {
        IssueChangeStatusRequest request = new IssueChangeStatusRequest();
        String token = "Bearer " + jwtProvider.generateAccessToken(new User());
        mockMvc.perform(patch("/row/change-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeStatusShouldReturnBadRequestForWithoutAuthorization() throws Exception {
        mockMvc.perform(patch("/row/change-status"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateRowShouldReturnOk() throws Exception {
        IssueChangeRequest request = new IssueChangeRequest();
        request.setTitle("Название задачи.");
        request.setStatus("WAIT");
        request.setPriority("HIGH");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setPassword("best1password");
        savedUser.setEmail("olezhka@gmail.com");
        savedUser.setLogin("Olezhka");
        User savedSup = new User();
        savedSup.setId(2L);
        savedSup.setPassword("best1password");
        savedSup.setEmail("olezhka@gmail.com");
        savedSup.setLogin("Olezhka");

        Issue issue = new Issue();
        issue.setId(1L);
        issue.setTitle("Title");
        issue.setDescription("Описание задачи.");
        issue.setPriority(Priority.valueOf("HIGH"));
        issue.setStatus(Status.valueOf("WAIT"));
        issue.setAuthor(savedUser);
        issue.setSupplier(savedSup);

        when(issueService.changeIssue(request)).thenReturn(issue);

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(patch("/row/update-row")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(issue.getTitle()));

        verify(issueService).changeIssue(request);
    }

    @Test
    void updateRowShouldReturnBadRequestForInvalidInput() throws Exception {
        IssueChangeRequest request = new IssueChangeRequest();

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(patch("/row/update-row")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateRowShouldReturnBadRequestForWithoutAuthorization() throws Exception {
        mockMvc.perform(patch("/row/update-row"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteIssueShouldReturnOk() throws Exception {
        IssueDeleteRequest request = new IssueDeleteRequest();
        request.setTitle("Название задачи.");
        doNothing().when(issueService).deleteIssue(request);

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(delete("/row")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Row successfully deleted"));

        verify(issueService).deleteIssue(request);
    }

    @Test
    void deleteIssueShouldReturnBadRequestForInvalidInput() throws Exception {
        IssueDeleteRequest request = new IssueDeleteRequest();
        String token = "Bearer " + jwtProvider.generateAccessToken(new User());
        mockMvc.perform(delete("/row")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByAuthorOrSupplierLoginShouldReturnOk() throws Exception {
        PaginationParams paginationParams = new PaginationParams();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Issue> rowsPage = new PageImpl<>(List.of());

        when(pageableCreator.create(paginationParams)).thenReturn(pageable);
        when(issueService.getIssuesByUserLogin("author", pageable)).thenReturn(rowsPage);

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(get("/row")
                        .param("user-login", "author")
                        .header("Authorization", token)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(issueService).getIssuesByUserLogin("author", pageable);
    }

    @Test
    void getByAuthorOrSupplierLoginShouldReturnBadRequest() throws Exception {
        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(get("/row")
                        .param("user-login", "fedya")
                        .header("Authorization", token)
                        .param("page", "-1")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByAuthorOrSupplierLoginShouldReturnNotFound() throws Exception {
        PaginationParams paginationParams = new PaginationParams();
        Pageable pageable = PageRequest.of(0, 10);

        when(pageableCreator.create(paginationParams)).thenReturn(pageable);
        when(issueService.getIssuesByUserLogin("author", pageable)).thenThrow(new NotFoundException("Not found!"));

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(get("/row")
                        .param("user-login", "author")
                        .header("Authorization", token)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound());

        verify(issueService).getIssuesByUserLogin("author", pageable);
    }

    @Test
    void getByAuthorOrSupplierLoginShouldReturnForbiddenWhenUnauthorized() throws Exception {
        mockMvc.perform(get("/row")
                        .param("user-login", "author")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isForbidden());
    }
}

