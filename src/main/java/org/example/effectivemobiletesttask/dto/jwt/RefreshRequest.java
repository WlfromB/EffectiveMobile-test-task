package org.example.effectivemobiletesttask.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос для обновления токена. Применяется для access и refresh токенов.")
public class RefreshRequest {
    @Schema(description = "Строка заключающая токен (access/refresh).", example = "super.secret.token")
    String token;
}
