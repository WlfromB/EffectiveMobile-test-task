package org.example.effectivemobiletesttask.services.comment;

import org.example.effectivemobiletesttask.entities.Comment;
import org.example.effectivemobiletesttask.dto.comment.*;

public interface CommentService {
    Comment createComment(CommentCreateRequest request) throws Exception;
}
