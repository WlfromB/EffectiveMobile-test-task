package org.example.effectivemobiletesttask.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.effectivemobiletesttask.entities.User;

@Data
@Schema(description = "Ответ API на запросы по эндпоинтам, начинающимся с /user")
public class UserResponse {
    @Schema(description = "Логин пользователя.", example = "Olezhka")
    private String login;
    @Schema(description = "Email пользователя.", example = "Olezhka@gmail.com")
    private String email;
    
    public UserResponse(User user) {
        this.login = user.getLogin();
        this.email = user.getEmail();
    }
}
