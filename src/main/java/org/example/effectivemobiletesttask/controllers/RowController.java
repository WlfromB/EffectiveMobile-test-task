package org.example.effectivemobiletesttask.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.effectivemobiletesttask.dto.row.*;
import org.example.effectivemobiletesttask.entities.Row;
import org.example.effectivemobiletesttask.pagination.PageableCreator;
import org.example.effectivemobiletesttask.pagination.PaginationParams;
import org.example.effectivemobiletesttask.security.JwtAuthentication;
import org.example.effectivemobiletesttask.services.row.RowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/row")
@Tag(name = "Методы работы с задачами.")
@Slf4j
public class RowController {
    private final RowService rowService;
    private final PageableCreator pageableCreator;

    @PostMapping
    @Operation(
            summary = "Создание задачи.",
            description = "Позволяет создать задачу. Требует авторизации."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<RowResponse> addRow(
            @Valid @RequestBody RowCreateRequest request) throws Exception {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        request.setAuthorLogin(authentication.getName());
        Row row = rowService.createRow(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(row.getId())
                .toUri();
        return ResponseEntity.created(location).body(new RowResponse(row));
    }

    @PatchMapping("/change-status")
    @Operation(
            summary = "Изменение статуса задачи.",
            description = "Позволяет изменить статус задачи. Требует авторизации. " +
                    "Может быть использовано автором задачи или ее исполнителем."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<RowResponse> changeStatus(
            @Valid @RequestBody RowChangeStatusRequest request) throws Exception {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        request.setLogin(authentication.getName());
        Row row = rowService.changeStatus(request);
        return ResponseEntity.ok(new RowResponse(row));
    }

    @PatchMapping("/update-row")
    @Operation(
            summary = "Изменение данных задачи.",
            description = "Позволяет изменить данные задачи. Требует авторизации. " +
                    "Может быть использовано только автором задачи."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<RowResponse> updateRow(
            @Valid @RequestBody RowChangeRequest request) throws Exception {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        request.setLogin(authentication.getName());
        Row row = rowService.changeRow(request);
        return ResponseEntity.ok(new RowResponse(row));
    }

    @DeleteMapping
    @Operation(
            summary = "Удаление задачи.",
            description = "Позволяет удалить задачу. Требует авторизации. " +
                    "Может быть использовано только автором задачи."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> deleteRow(
            @Valid @RequestBody RowDeleteRequest request) throws Exception {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        request.setAuthor(authentication.getName());
        System.out.println("before");
        rowService.deleteRow(request);
        return ResponseEntity.ok("Row successfully deleted");
    }

    @GetMapping
    @Operation(
            summary = "Получение задачи.",
            description = "Позволяет получить задачу. Требует авторизации. " +
                    "Может быть использовано любым авторизованным пользователем."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Page<RowResponse>> getByAuthorOrSupplierLogin(
            @Valid @RequestParam(name = "user-login") @Parameter(description = "Логин пользователя", example = "author") String login,
            @Valid @ModelAttribute @Parameter(description = "Параметры пагинации.") PaginationParams paginationParams) throws Exception {
        Pageable pageable = pageableCreator.create(paginationParams);
        Page<Row> rows = rowService.getRowsByUserLogin(login, pageable);
        return ResponseEntity.ok(rows.map(RowResponse::new));
    }
}
