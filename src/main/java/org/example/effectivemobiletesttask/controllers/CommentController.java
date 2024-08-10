package org.example.effectivemobiletesttask.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletesttask.dto.comment.CommentCreateRequest;
import org.example.effectivemobiletesttask.dto.comment.CommentResponse;
import org.example.effectivemobiletesttask.entities.Comment;
import org.example.effectivemobiletesttask.services.comment.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Tag(name = "Методы работы с комментариями")
public class CommentController {
    private final CommentService commentService;
    
    @PostMapping
    @Operation(
            summary = "Создание комментария",
            description = "Позволяет создать комментарий. Требует авторизации."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<CommentResponse> createComment(
            @Valid @RequestBody CommentCreateRequest request) throws Exception {
        Comment comment = commentService.createComment(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(comment.getId())
                .toUri();
        return ResponseEntity.created(location).body(new CommentResponse(comment.getText(), comment.getRow().getTitle()));
    } 
}
