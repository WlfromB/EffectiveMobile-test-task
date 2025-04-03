package org.example.effectivemobiletesttask.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.effectivemobiletesttask.dto.issue.*;
import org.example.effectivemobiletesttask.entities.Issue;
import org.example.effectivemobiletesttask.pagination.PageableCreator;
import org.example.effectivemobiletesttask.pagination.PaginationParams;
import org.example.effectivemobiletesttask.security.JwtAuthentication;
import org.example.effectivemobiletesttask.services.row.IssueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/issue")
@Tag(name = "Методы работы с задачами.")
@Slf4j
public class IssueController {
    private final IssueService issueService;
    private final PageableCreator pageableCreator;

    @PostMapping
    @Operation(
            summary = "Создание задачи.",
            description = "Позволяет создать задачу. Требует авторизации."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<IssueResponse> addIssue(
            @Valid @RequestBody IssueCreateRequest request) throws Exception {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        request.setAuthorLogin(authentication.getName());
        Issue issue = issueService.createIssue(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(issue.getId())
                .toUri();
        return ResponseEntity.created(location).body(IssueResponse.fromIssue(issue));
    }

    @PatchMapping("/change-status")
    @Operation(
            summary = "Изменение статуса задачи.",
            description = "Позволяет изменить статус задачи. Требует авторизации. " +
                    "Может быть использовано автором задачи или ее исполнителем."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<IssueResponse> changeStatus(
            @Valid @RequestBody IssueChangeStatusRequest request) throws Exception {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        request.setLogin(authentication.getName());
        Issue issue = issueService.changeStatus(request);
        return ResponseEntity.ok(IssueResponse.fromIssue(issue));
    }

    @PatchMapping("/update-issue")
    @Operation(
            summary = "Изменение данных задачи.",
            description = "Позволяет изменить данные задачи. Требует авторизации. " +
                    "Может быть использовано только автором задачи."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<IssueResponse> updateIssue(
            @Valid @RequestBody IssueChangeRequest request) throws Exception {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        request.setLogin(authentication.getName());
        Issue issue = issueService.changeIssue(request);
        return ResponseEntity.ok(IssueResponse.fromIssue(issue));
    }

    @DeleteMapping
    @Operation(
            summary = "Удаление задачи.",
            description = "Позволяет удалить задачу. Требует авторизации. " +
                    "Может быть использовано только автором задачи."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> deleteIssue(
            @Valid @RequestBody IssueDeleteRequest request) throws Exception {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        request.setAuthor(authentication.getName());
        issueService.deleteIssue(request);
        return ResponseEntity.ok("Issue successfully deleted");
    }

    @GetMapping
    @Operation(
            summary = "Получение задачи.",
            description = "Позволяет получить задачу. Требует авторизации. " +
                    "Может быть использовано любым авторизованным пользователем."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Page<IssueResponse>> getByAuthorOrSupplierLogin(
            @Valid @RequestParam(name = "user-login") @Parameter(description = "Логин пользователя", example = "author") String login,
            @Valid @ModelAttribute @Parameter(description = "Параметры пагинации.") PaginationParams paginationParams) throws Exception {
        Pageable pageable = pageableCreator.create(paginationParams);
        Page<Issue> issues = issueService.getIssuesByUserLogin(login, pageable);
        return ResponseEntity.ok(issues.map(IssueResponse::fromIssue));
    }
}
