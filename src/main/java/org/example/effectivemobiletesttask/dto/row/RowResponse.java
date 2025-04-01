package org.example.effectivemobiletesttask.dto.row;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.effectivemobiletesttask.dto.comment.CommentResponse;
import org.example.effectivemobiletesttask.entities.Priority;
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

    public static RowResponse fromRow(Row row) {
        RowResponse responseObject = new RowResponse();
        setFieldsFromIssue(row, responseObject);
        return responseObject;
    }

    private void setPriority(Priority priority){
        this.priority = priority.toString();
    }

    private static void setFieldsFromIssue(Row issue, RowResponse responseObject) {
        String title = issue.getTitle();

        responseObject.setTitle(title);
        responseObject.setDescription(issue.getDescription());
        responseObject.setAuthor(issue.getLoginAuthor());
        responseObject.setSupplier(issue.getLoginSupplier());
        responseObject.setStatus(issue.getStatus());
        responseObject.setPriority(issue.getPriority());

        setCommentsFromIssue(issue, responseObject);
    }

    private static void setCommentsFromIssue(Row issue, RowResponse responseObject) {
        String title = issue.getTitle();
        responseObject.comments = issue.getComments().stream()
                .map(comment -> new CommentResponse(title, comment.getText()))
                .collect(Collectors.toSet());
    }
}
