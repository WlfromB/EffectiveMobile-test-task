package org.example.effectivemobiletesttask.services;

import org.example.effectivemobiletesttask.dao.RowRepository;
import org.example.effectivemobiletesttask.dto.row.RowChangeRequest;
import org.example.effectivemobiletesttask.dto.row.RowChangeStatusRequest;
import org.example.effectivemobiletesttask.dto.row.RowCreateRequest;
import org.example.effectivemobiletesttask.dto.row.RowDeleteRequest;
import org.example.effectivemobiletesttask.entities.Row;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.services.row.RowServiceImpl;
import org.example.effectivemobiletesttask.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.webjars.NotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class RowServiceTests {

    @Mock
    private RowRepository rowRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RowServiceImpl rowService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRow_RowAlreadyExists_ShouldThrowIllegalArgumentException() {
        RowCreateRequest request = new RowCreateRequest();
        request.setTitle("ExistingTitle");
        when(rowRepository.findByTitle(request.getTitle())).thenReturn(Optional.of(new Row()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rowService.createRow(request));
        assertEquals("Row with title ExistingTitle already exists", exception.getMessage());
    }
    
    @Test
    void changeStatus_RowNotFound_ShouldThrowNotFoundException() {
        RowChangeStatusRequest request = new RowChangeStatusRequest();
        request.setTitle("NonExistentTitle");
        when(rowRepository.findByTitle(request.getTitle())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> rowService.changeStatus(request));
        assertEquals("Row not found", exception.getMessage());
    }

    @Test
    void changeStatus_UserNotAuthorOrSupplier_ShouldThrowIllegalArgumentException() {
        Row row = new Row();
        User author = new User();
        author.setLogin("authorLogin");
        row.setAuthor(author);
        User supplier = new User();
        supplier.setLogin("authorLogin");
        row.setSupplier(supplier);
        RowChangeStatusRequest request = new RowChangeStatusRequest();
        request.setTitle("Title");
        request.setLogin("otherUser");
        when(rowRepository.findByTitle(request.getTitle())).thenReturn(Optional.of(row));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rowService.changeStatus(request));
        assertEquals("User not a author or supplier!", exception.getMessage());
    }

    @Test
    void changeRow_RowNotFound_ShouldThrowNotFoundException() {
        RowChangeRequest request = new RowChangeRequest();
        request.setTitle("NonExistentTitle");
        when(rowRepository.findByTitle(request.getTitle())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> rowService.changeRow(request));
        assertEquals("Row not found", exception.getMessage());
    }

    @Test
    void changeRow_UserNotAuthor_ShouldThrowIllegalArgumentException() {
        Row row = new Row();
        User author = new User();
        author.setLogin("authorLogin");
        row.setAuthor(author);
        RowChangeRequest request = new RowChangeRequest();
        request.setTitle("Title");
        request.setLogin("otherUser");
        when(rowRepository.findByTitle(request.getTitle())).thenReturn(Optional.of(row));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rowService.changeRow(request));
        assertEquals("This user is not the author", exception.getMessage());
    }

    @Test
    void deleteRow_RowNotFound_ShouldThrowNotFoundException() {
        RowDeleteRequest request = new RowDeleteRequest();
        request.setTitle("NonExistentTitle");
        when(rowRepository.findByTitle(request.getTitle())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> rowService.deleteRow(request));
        assertEquals("Row not found", exception.getMessage());
    }

    @Test
    void deleteRow_UserNotAuthor_ShouldThrowIllegalArgumentException() throws Exception {
        Row row = new Row();
        User author = new User();
        author.setLogin("authorLogin");
        author.setId(1L);
        User other = new User();
        other.setLogin("otherLogin");
        other.setId(2L);
        row.setAuthor(author);
        RowDeleteRequest request = new RowDeleteRequest();
        request.setTitle("Title");
        request.setAuthor("otherUser");
        when(rowRepository.findByTitle(request.getTitle())).thenReturn(Optional.of(row));
        when(userService.findByLogin(request.getAuthor())).thenReturn(other);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rowService.deleteRow(request));
        assertEquals("This user is not the author", exception.getMessage());
    }

    @Test
    void getRowsByUserLogin_ShouldReturnRows() throws Exception {
        User user = new User();
        user.setLogin("userLogin");
        Page<Row> rows = new PageImpl<>(Collections.singletonList(new Row()));
        when(userService.findByLogin(user.getLogin())).thenReturn(user);
        when(rowRepository.findAllByAuthorOrSupplier(user, user, Pageable.unpaged())).thenReturn(rows);

        Page<Row> result = rowService.getRowsByUserLogin(user.getLogin(), Pageable.unpaged());

        assertEquals(rows, result);
    }

    @Test
    void getByTitle_RowNotFound_ShouldThrowNotFoundException() {
        when(rowRepository.findByTitle("NonExistentTitle")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> rowService.getByTitle("NonExistentTitle"));
        assertEquals("Row not found", exception.getMessage());
    }
}