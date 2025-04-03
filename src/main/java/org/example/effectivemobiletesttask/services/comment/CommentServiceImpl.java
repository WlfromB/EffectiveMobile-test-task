package org.example.effectivemobiletesttask.services.comment;


import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletesttask.dao.CommentRepository;
import org.example.effectivemobiletesttask.dao.IssueRepository;
import org.example.effectivemobiletesttask.dto.comment.CommentCreateRequest;
import org.example.effectivemobiletesttask.entities.Comment;
import org.example.effectivemobiletesttask.entities.Issue;
import org.example.effectivemobiletesttask.services.row.IssueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final IssueService issueService;
    private final IssueRepository issueRepository;
    
    @Override
    @Transactional
    public Comment createComment(CommentCreateRequest request) throws Exception {
        String issueTitle = request.getRowTitle();
        Issue issue = issueService.getByTitle(issueTitle);
        Comment comment = CommentCreateRequest.toCommentFromRowAndCommentCreateRequest(request, issue);
        issueRepository.save(issue);
        return commentRepository.save(comment);
    }
}

