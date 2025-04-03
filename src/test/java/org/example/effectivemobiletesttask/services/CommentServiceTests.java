package org.example.effectivemobiletesttask.services;

import org.example.effectivemobiletesttask.dao.CommentRepository;
import org.example.effectivemobiletesttask.dao.IssueRepository;
import org.example.effectivemobiletesttask.dto.comment.CommentCreateRequest;
import org.example.effectivemobiletesttask.entities.Comment;
import org.example.effectivemobiletesttask.entities.Issue;
import org.example.effectivemobiletesttask.services.comment.CommentServiceImpl;
import org.example.effectivemobiletesttask.services.row.IssueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.webjars.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentServiceTests {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IssueService issueService;

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createComment_ShouldCreateAndReturnComment() throws Exception {
        String rowTitle = "Test Row";
        CommentCreateRequest request = new CommentCreateRequest();
        request.setRowTitle(rowTitle);
        request.setComment("Test Content");

        Issue issue = new Issue();
        issue.setTitle(rowTitle);

        Comment comment = new Comment();
        comment.setText("Test Content");
        comment.setIssue(issue);

        when(issueService.getByTitle(rowTitle)).thenReturn(issue);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);


        Comment result = commentService.createComment(request);
        
        assertNotNull(result);
        assertEquals("Test Content", result.getText());
        assertEquals(rowTitle, result.getIssue().getTitle());

        verify(issueService).getByTitle(rowTitle);
        verify(commentRepository).save(any(Comment.class));
        verify(issueRepository).save(issue);
    }

    @Test
    void createComment_WhenRowNotFound_ShouldThrowNotFoundException() throws Exception {
        String rowTitle = "Nonexistent Row";
        CommentCreateRequest request = new CommentCreateRequest();
        request.setRowTitle(rowTitle);

        when(issueService.getByTitle(rowTitle)).thenThrow(new NotFoundException("Row not found"));
        
        assertThrows(NotFoundException.class, () -> commentService.createComment(request));

        verify(issueService).getByTitle(rowTitle);
        verifyNoInteractions(commentRepository);
        verifyNoInteractions(issueRepository);
    }
}