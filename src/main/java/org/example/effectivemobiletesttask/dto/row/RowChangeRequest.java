package org.example.effectivemobiletesttask.dto.row;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.effectivemobiletesttask.validation.priority.ValidPriority;
import org.example.effectivemobiletesttask.validation.status.ValidStatus;

@Data
@Schema(description = "Запрос на изменение данных задачи.")
public class RowChangeRequest { 
    @NotBlank(message = "Не должен быть пустым. Должно быть название существующей задачи.")
    @Schema(description = "Название задачи.", example = "Название задачи.")
    private String title;
    @Schema(description = "Описание задачи.", example = "Описание задачи.")
    private String description;
    @ValidPriority
    @Schema(description = "Приоритет выполнения задачи.", example = "HIGH")
    private String priority;
    @Hidden
    @Schema(description = "Логин пользователя. Заполнение произойдет из security context.")
    private String login;
    @Schema(description = "Логин пользователя-выполнителя задачи.", example = "NeOlezhka")
    private String supplier;
    @ValidStatus
    @Schema(description = "Статус задачи.", example = "COMPLETED")
    private String status;
}
