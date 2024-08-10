package org.example.effectivemobiletesttask.dto.row;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.effectivemobiletesttask.dto.comment.CommentResponse;
import org.example.effectivemobiletesttask.entities.Row;
import org.example.effectivemobiletesttask.entities.Status;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Schema(description = "Ответ API для запросов задач.")
public class RowResponse {
    @Schema(description = "Название задачи.", example = "Название задачи.")
    private String title;
    @Schema(description = "Описание задачи.", example = "Описание задачи.")
    private String description;
    @Schema(description = "Автор задачи.(логин)", example = "Olezhka")
    private String author;
    @Schema(description = "Выполнитель задачи.(логин)", example = "NeOlezhka")
    private String supplier;
    @Schema(description = "Статус задачи.", example = "COMPLETED")
    private Status status;
    @Schema(description = "Приоритет задачи.", example = "HIGH")
    private String priority;
    @Schema(description = "Комментарии к задаче.")
    private Set<CommentResponse> comments;

    public RowResponse(Row row) {
        this.title = row.getTitle();
        this.description = row.getDescription();
        this.author = row.getAuthor().getLogin();
        this.supplier = row.getSupplier().getLogin();
        this.status = row.getStatus();
        this.priority = row.getPriority().toString();
        this.comments = row.getComments().stream()
                .map(comment -> new CommentResponse(title, comment.getText()))
                .collect(Collectors.toSet());
    }
}
