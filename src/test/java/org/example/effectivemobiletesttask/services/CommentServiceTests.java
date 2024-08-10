package org.example.effectivemobiletesttask.services;

import org.example.effectivemobiletesttask.dao.CommentRepository;
import org.example.effectivemobiletesttask.dao.RowRepository;
import org.example.effectivemobiletesttask.dto.comment.CommentCreateRequest;
import org.example.effectivemobiletesttask.entities.Comment;
import org.example.effectivemobiletesttask.entities.Row;
import org.example.effectivemobiletesttask.services.comment.CommentServiceImpl;
import org.example.effectivemobiletesttask.services.row.RowService;
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
    private RowService rowService;

    @Mock
    private RowRepository rowRepository;

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

        Row row = new Row();
        row.setTitle(rowTitle);

        Comment comment = new Comment();
        comment.setText("Test Content");
        comment.setRow(row);

        when(rowService.getByTitle(rowTitle)).thenReturn(row);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);


        Comment result = commentService.createComment(request);
        
        assertNotNull(result);
        assertEquals("Test Content", result.getText());
        assertEquals(rowTitle, result.getRow().getTitle());

        verify(rowService).getByTitle(rowTitle);
        verify(commentRepository).save(any(Comment.class));
        verify(rowRepository).save(row);
    }

    @Test
    void createComment_WhenRowNotFound_ShouldThrowNotFoundException() throws Exception {
        String rowTitle = "Nonexistent Row";
        CommentCreateRequest request = new CommentCreateRequest();
        request.setRowTitle(rowTitle);

        when(rowService.getByTitle(rowTitle)).thenThrow(new NotFoundException("Row not found"));
        
        assertThrows(NotFoundException.class, () -> commentService.createComment(request));

        verify(rowService).getByTitle(rowTitle);
        verifyNoInteractions(commentRepository);
        verifyNoInteractions(rowRepository);
    }
}