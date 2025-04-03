package org.example.effectivemobiletesttask.services;

import org.example.effectivemobiletesttask.dao.IssueRepository;
import org.example.effectivemobiletesttask.dto.issue.IssueChangeRequest;
import org.example.effectivemobiletesttask.dto.issue.IssueChangeStatusRequest;
import org.example.effectivemobiletesttask.dto.issue.IssueCreateRequest;
import org.example.effectivemobiletesttask.dto.issue.IssueDeleteRequest;
import org.example.effectivemobiletesttask.entities.Issue;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.services.row.IssueServiceImpl;
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

class IssueServiceTests {

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private IssueServiceImpl rowService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createIssue_RowAlreadyExists_ShouldThrowIllegalArgumentException() {
        IssueCreateRequest request = new IssueCreateRequest();
        request.setTitle("ExistingTitle");
        when(issueRepository.findByTitle(request.getTitle())).thenReturn(Optional.of(new Issue()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rowService.createIssue(request));
        assertEquals("Row with title ExistingTitle already exists", exception.getMessage());
    }
    
    @Test
    void changeStatus_RowNotFound_ShouldThrowNotFoundException() {
        IssueChangeStatusRequest request = new IssueChangeStatusRequest();
        request.setTitle("NonExistentTitle");
        when(issueRepository.findByTitle(request.getTitle())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> rowService.changeStatus(request));
        assertEquals("Row not found", exception.getMessage());
    }

    @Test
    void changeStatus_UserNotAuthorOrSupplier_ShouldThrowIllegalArgumentException() {
        Issue issue = new Issue();
        User author = new User();
        author.setLogin("authorLogin");
        issue.setAuthor(author);
        User supplier = new User();
        supplier.setLogin("authorLogin");
        issue.setSupplier(supplier);
        IssueChangeStatusRequest request = new IssueChangeStatusRequest();
        request.setTitle("Title");
        request.setLogin("otherUser");
        when(issueRepository.findByTitle(request.getTitle())).thenReturn(Optional.of(issue));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rowService.changeStatus(request));
        assertEquals("User not a author or supplier!", exception.getMessage());
    }

    @Test
    void changeIssue_RowNotFound_ShouldThrowNotFoundException() {
        IssueChangeRequest request = new IssueChangeRequest();
        request.setTitle("NonExistentTitle");
        when(issueRepository.findByTitle(request.getTitle())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> rowService.changeIssue(request));
        assertEquals("Row not found", exception.getMessage());
    }

    @Test
    void changeIssue_UserNotAuthor_ShouldThrowIllegalArgumentException() {
        Issue issue = new Issue();
        User author = new User();
        author.setLogin("authorLogin");
        issue.setAuthor(author);
        IssueChangeRequest request = new IssueChangeRequest();
        request.setTitle("Title");
        request.setLogin("otherUser");
        when(issueRepository.findByTitle(request.getTitle())).thenReturn(Optional.of(issue));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rowService.changeIssue(request));
        assertEquals("This user is not the author", exception.getMessage());
    }

    @Test
    void deleteIssue_RowNotFound_ShouldThrowNotFoundException() {
        IssueDeleteRequest request = new IssueDeleteRequest();
        request.setTitle("NonExistentTitle");
        when(issueRepository.findByTitle(request.getTitle())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> rowService.deleteIssue(request));
        assertEquals("Row not found", exception.getMessage());
    }

    @Test
    void deleteIssue_UserNotAuthor_ShouldThrowIllegalArgumentException() throws Exception {
        Issue issue = new Issue();
        User author = new User();
        author.setLogin("authorLogin");
        author.setId(1L);
        User other = new User();
        other.setLogin("otherLogin");
        other.setId(2L);
        issue.setAuthor(author);
        IssueDeleteRequest request = new IssueDeleteRequest();
        request.setTitle("Title");
        request.setAuthor("otherUser");
        when(issueRepository.findByTitle(request.getTitle())).thenReturn(Optional.of(issue));
        when(userService.findByLogin(request.getAuthor())).thenReturn(other);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rowService.deleteIssue(request));
        assertEquals("This user is not the author", exception.getMessage());
    }

    @Test
    void getIssuesByUserLogin_ShouldReturnRows() throws Exception {
        User user = new User();
        user.setLogin("userLogin");
        Page<Issue> rows = new PageImpl<>(Collections.singletonList(new Issue()));
        when(userService.findByLogin(user.getLogin())).thenReturn(user);
        when(issueRepository.findAllByAuthorOrSupplier(user, user, Pageable.unpaged())).thenReturn(rows);

        Page<Issue> result = rowService.getIssuesByUserLogin(user.getLogin(), Pageable.unpaged());

        assertEquals(rows, result);
    }

    @Test
    void getByTitle_RowNotFound_ShouldThrowNotFoundException() {
        when(issueRepository.findByTitle("NonExistentTitle")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> rowService.getByTitle("NonExistentTitle"));
        assertEquals("Row not found", exception.getMessage());
    }
}