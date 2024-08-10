package org.example.effectivemobiletesttask.services.comment;


import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletesttask.dao.CommentRepository;
import org.example.effectivemobiletesttask.dao.RowRepository;
import org.example.effectivemobiletesttask.dto.comment.CommentCreateRequest;
import org.example.effectivemobiletesttask.entities.Comment;
import org.example.effectivemobiletesttask.entities.Row;
import org.example.effectivemobiletesttask.services.row.RowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final RowService rowService;
    private final RowRepository rowRepository;
    
    @Override
    @Transactional
    public Comment createComment(CommentCreateRequest request) throws Exception {
        Row row = rowService.getByTitle(request.getRowTitle());
        Comment comment = request.from();
        comment.setRow(row);
        rowRepository.save(row);
        return commentRepository.save(comment);
    }
}

