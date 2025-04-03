package org.example.effectivemobiletesttask.dto.issue;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.effectivemobiletesttask.entities.Priority;
import org.example.effectivemobiletesttask.entities.Issue;
import org.example.effectivemobiletesttask.validation.priority.ValidPriority;

@Data
@Schema(description = "Запрос на создание задачи.")
public class IssueCreateRequest {
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

    public Issue from() {
        Issue issue = new Issue();
        issue.setTitle(title);
        issue.setDescription(description);
        issue.setPriority(Priority.valueOf(priority));
        return issue;
    }
}
