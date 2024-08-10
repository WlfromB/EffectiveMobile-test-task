package org.example.effectivemobiletesttask.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletesttask.entities.User;

@Data
@RequiredArgsConstructor
@Schema(description = "Запрос на создание пользователя.")
public class UserRequestCreate {
    @NotBlank(message = "Логин не может быть пустым. Минимальный (кол-во символов) - 5, максимальный - 20")
    @Size(min = 5, max = 20)
    @Schema(description = "Логин пользователя.", example = "Olezhka")
    private String login;

    @NotBlank(message = "Email не может быть пустым. Пример: oleg@gmail.com")
    @Pattern(regexp = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$")
    @Schema(description = "Email пользователя.", example = "Olezhka@gmail.com")
    private String email;

    @NotBlank(message = "Не может быть пустым. Минимальный (кол-во символов) - 8, максимальный - 20")
    @Size(min = 8, max = 20)
    @Schema(description = "Пароль пользователя.", example = "best1password")
    private String password;
    
    public User from(){
        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        return user;
    }
}
