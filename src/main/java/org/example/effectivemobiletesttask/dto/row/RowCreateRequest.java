package org.example.effectivemobiletesttask.dto.row;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.effectivemobiletesttask.entities.Priority;
import org.example.effectivemobiletesttask.entities.Row;
import org.example.effectivemobiletesttask.validation.priority.ValidPriority;

@Data
@Schema(description = "Запрос на создание задачи.")
public class RowCreateRequest {
    @NotBlank(message = "Не должен быть пустым. Должно быть название существующей задачи.")
    @Schema(description = "Название задачи.", example = "Название задачи.")
    private String title;
    @NotBlank
    @Schema(description = "Описание задачи.", example = "Описание задачи.")
    private String description;
    @Hidden
    @Schema(description = "Логин пользователя-автора. Заполнение произойдет из security context.", example = "Olezhka")
    private String authorLogin;
    @NotBlank
    @Schema(description = "Логин пользователя-выполнителя задачи.", example = "NeOlezhka")
    private String supplierLogin;
    @NotBlank
    @ValidPriority
    @Schema(description = "Приоритет выполнения задачи.", example = "HIGH")
    private String priority;

    public Row from() {
        Row row = new Row();
        row.setTitle(title);
        row.setDescription(description);
        row.setPriority(Priority.valueOf(priority));
        return row;
    }
}
