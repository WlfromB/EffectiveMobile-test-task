package org.example.effectivemobiletesttask.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletesttask.dto.jwt.JwtRequest;
import org.example.effectivemobiletesttask.dto.jwt.JwtResponse;
import org.example.effectivemobiletesttask.dto.jwt.RefreshRequest;
import org.example.effectivemobiletesttask.services.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Методы работы с авторизацией")
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    @Operation(
            summary = "Авторизация пользователя",
            description = "Позволяет получить access и refresh jwts"
    )
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody JwtRequest request) throws Exception {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("access")
    @Operation(
            summary = "Получение access токена",
            description = "Позволяет обновить access токен, в случае валидного и not exprired refresh токена"
    )
    public ResponseEntity<JwtResponse> access(
            @Valid @RequestBody RefreshRequest request) throws Exception {
        return ResponseEntity.ok(authService.getAccessToken(request.getToken()));
    }

    @PostMapping("refresh")
    @Operation(
            summary = "Получение refresh токена",
            description = "Позволяет обновить refresh токен, в случае валидного и not exprired refresh токена"
    )
    public ResponseEntity<JwtResponse> refresh(
            @Valid @RequestBody RefreshRequest request) throws Exception {
        return ResponseEntity.ok(authService.refresh(request.getToken()));
    }
}
