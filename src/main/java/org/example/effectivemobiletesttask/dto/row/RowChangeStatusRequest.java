package org.example.effectivemobiletesttask.dto.row;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.effectivemobiletesttask.validation.status.ValidStatus;

@Data
@Schema(description = "Запрос на изменение статуса задачи.")
public class RowChangeStatusRequest {
    @NotBlank(message = "Не должен быть пустым. Должно быть название существующей задачи.")
    @Schema(description = "Название задачи.", example = "Название задачи.")
    private String title;
    @NotBlank(message = "Не должно быть пустым.")
    @ValidStatus
    @Schema(description = "Статус задачи.", example = "COMPLETED")
    private String status;
    @Hidden
    @Schema(description = "Логин пользователя. Заполнение произойдет из security context.")
    private String login;
}
