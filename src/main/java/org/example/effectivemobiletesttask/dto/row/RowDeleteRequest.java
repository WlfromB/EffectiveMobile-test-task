package org.example.effectivemobiletesttask.dto.row;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на удаление задачи.")
public class RowDeleteRequest {
    @Hidden
    @Schema(description = "Логин пользователя-автора. Заполнение произойдет из security context.")
    private String author;
    @NotBlank(message = "Не должен быть пустым. Должно быть название существующей задачи.")
    @Schema(description = "Название задачи.", example = "Название задачи.")
    private String title;
}
