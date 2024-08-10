package org.example.effectivemobiletesttask.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletesttask.dto.user.UserRequestCreate;
import org.example.effectivemobiletesttask.dto.user.UserResponse;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.pagination.PageableCreator;
import org.example.effectivemobiletesttask.pagination.PaginationParams;
import org.example.effectivemobiletesttask.services.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "Методы работы с пользователем.")
public class UserController {
    private final UserService userService;
    private final PageableCreator pageableCreator;

    @PostMapping
    @Operation(
            summary = "Создание пользователя.",
            description = "Позволяет создать пользователя. Не требует авторизации."
    )
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRequestCreate requestCreate) throws Exception {
        User savedUser = userService.createUser(requestCreate);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).body("successfully created user");
    }

    @GetMapping("/users")
    @Operation(
            summary = "Получение пользователей.",
            description = "Позволяет получить страницу пользователей. Требует авторизации.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @Valid @ModelAttribute @Parameter(description = "Параметры пагинации.") PaginationParams paginationParams)
            throws Exception {
        Pageable pageable = pageableCreator.create(paginationParams);
        return ResponseEntity.ok(userService.findAll(pageable).map(UserResponse::new));
    }

    @GetMapping
    @Operation(
            summary = "Получение пользователя.",
            description = "Позволяет получить пользователя по id. Требует авторизации."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<UserResponse> getUserById(@Valid @RequestParam @Min(1) @Parameter(description = "Id пользователя", example = "1") Long id) throws Exception {
        User user = userService.findById(id);
        return ResponseEntity.ok(new UserResponse(user));
    }

    @GetMapping("/login")
    @Operation(
            summary = "Получение пользователя.",
            description = "Позволяет получить пользователя по login. Требует авторизации."
    )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<UserResponse> getUserByLogin(@RequestParam @Parameter(description = "Login пользователя", example = "Olezhka") String login) throws Exception {
        User user = userService.findByLogin(login);
        return ResponseEntity.ok(new UserResponse(user));
    }
}
