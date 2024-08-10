package org.example.effectivemobiletesttask.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Схема запроса пользователя на авторизацию.")
public class JwtRequest {
    @NotBlank
    @Schema(description = "Логин или email пользователя.", example = "Olezhka")
    private String loginOrEmail;
    @NotBlank
    @Size(min = 8, max = 20)
    @Schema(description = "Пароль пользователя.", example = "12345678")
    private String password;
}
