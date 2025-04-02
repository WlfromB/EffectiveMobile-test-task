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

    public static UserResponse fromUser(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setDataFromUser(user);
        return userResponse;
    }

    public void setDataFromUser(User user) {
        String userLogin = user.getLogin();
        String userEmail = user.getEmail();
        this.setLogin(userLogin);
        this.setEmail(userEmail);
    }
}
