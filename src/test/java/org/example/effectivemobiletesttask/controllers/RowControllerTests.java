package org.example.effectivemobiletesttask.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.effectivemobiletesttask.dto.row.RowChangeRequest;
import org.example.effectivemobiletesttask.dto.row.RowChangeStatusRequest;
import org.example.effectivemobiletesttask.dto.row.RowCreateRequest;
import org.example.effectivemobiletesttask.dto.row.RowDeleteRequest;
import org.example.effectivemobiletesttask.entities.Priority;
import org.example.effectivemobiletesttask.entities.Row;
import org.example.effectivemobiletesttask.entities.Status;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.pagination.PageableCreator;
import org.example.effectivemobiletesttask.pagination.PaginationParams;
import org.example.effectivemobiletesttask.services.auth.JwtProvider;
import org.example.effectivemobiletesttask.services.row.RowService;
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
public class RowControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RowService rowService;

    @MockBean
    private PageableCreator pageableCreator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;


    @Test
    void addRowShouldReturnCreated() throws Exception {
        RowCreateRequest requestCreate = new RowCreateRequest();
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
        Row row = new Row();
        row.setId(1L);
        row.setAuthor(savedUser);
        row.setSupplier(savedSup);
        row.setTitle("Название задачи.");
        row.setDescription("Описание задачи.");
        row.setPriority(Priority.valueOf("HIGH"));
        row.setStatus(Status.valueOf("WAIT"));
        when(rowService.createRow(requestCreate)).thenReturn(row);

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(post("/row")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCreate))
                        .header("Authorization", token))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/row/1"))
                .andExpect(jsonPath("$.title").value(row.getTitle()));

        verify(rowService).createRow(requestCreate);
    }

    @Test
    void addRowShouldReturnBadRequestForInvalidInput() throws Exception {
        RowCreateRequest requestCreate = new RowCreateRequest();

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
        RowChangeStatusRequest request = new RowChangeStatusRequest();
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

        Row row = new Row();
        row.setId(1L);
        row.setAuthor(savedUser);
        row.setSupplier(suppl);
        row.setPriority(Priority.valueOf("HIGH"));
        row.setStatus(Status.valueOf("WAIT"));

        when(rowService.changeStatus(request)).thenReturn(row);

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(patch("/row/change-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(row.getTitle()));

        verify(rowService).changeStatus(request);
    }

    @Test
    void changeStatusShouldReturnBadRequestForInvalidInput() throws Exception {
        RowChangeStatusRequest request = new RowChangeStatusRequest();
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
        RowChangeRequest request = new RowChangeRequest();
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

        Row row = new Row();
        row.setId(1L);
        row.setTitle("Title");
        row.setDescription("Описание задачи.");
        row.setPriority(Priority.valueOf("HIGH"));
        row.setStatus(Status.valueOf("WAIT"));
        row.setAuthor(savedUser);
        row.setSupplier(savedSup);

        when(rowService.changeRow(request)).thenReturn(row);

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(patch("/row/update-row")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(row.getTitle()));

        verify(rowService).changeRow(request);
    }

    @Test
    void updateRowShouldReturnBadRequestForInvalidInput() throws Exception {
        RowChangeRequest request = new RowChangeRequest();

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
    void deleteRowShouldReturnOk() throws Exception {
        RowDeleteRequest request = new RowDeleteRequest();
        request.setTitle("Название задачи.");
        doNothing().when(rowService).deleteRow(request);

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(delete("/row")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Row successfully deleted"));

        verify(rowService).deleteRow(request);
    }

    @Test
    void deleteRowShouldReturnBadRequestForInvalidInput() throws Exception {
        RowDeleteRequest request = new RowDeleteRequest();
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
        Page<Row> rowsPage = new PageImpl<>(List.of());

        when(pageableCreator.create(paginationParams)).thenReturn(pageable);
        when(rowService.getRowsByUserLogin("author", pageable)).thenReturn(rowsPage);

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(get("/row")
                        .param("user-login", "author")
                        .header("Authorization", token)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(rowService).getRowsByUserLogin("author", pageable);
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
        when(rowService.getRowsByUserLogin("author", pageable)).thenThrow(new NotFoundException("Not found!"));

        String token = "Bearer " + jwtProvider.generateAccessToken(new User());

        mockMvc.perform(get("/row")
                        .param("user-login", "author")
                        .header("Authorization", token)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound());

        verify(rowService).getRowsByUserLogin("author", pageable);
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

