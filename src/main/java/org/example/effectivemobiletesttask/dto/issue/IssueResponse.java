package org.example.effectivemobiletesttask.dto.issue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.effectivemobiletesttask.dto.comment.CommentResponse;
import org.example.effectivemobiletesttask.entities.Priority;
import org.example.effectivemobiletesttask.entities.Issue;
import org.example.effectivemobiletesttask.entities.Status;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Schema(description = "Ответ API для запросов задач.")
public class IssueResponse {
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

    public static IssueResponse fromIssue(Issue issue) {
        IssueResponse responseObject = new IssueResponse();
        setFieldsFromIssue(issue, responseObject);
        return responseObject;
    }

    private void setPriority(Priority priority){
        this.priority = priority.toString();
    }

    private static void setFieldsFromIssue(Issue issue, IssueResponse responseObject) {
        String title = issue.getTitle();
        String description = issue.getDescription();
        String loginAuthor = issue.getLoginAuthor();
        String loginSupplier = issue.getLoginSupplier();
        Status issueStatus = issue.getStatus();
        Priority issuePriority = issue.getPriority();

        responseObject.setTitle(title);
        responseObject.setDescription(description);
        responseObject.setAuthor(loginAuthor);
        responseObject.setSupplier(loginSupplier);
        responseObject.setStatus(issueStatus);
        responseObject.setPriority(issuePriority);

        setCommentsFromIssue(issue, responseObject);
    }

    private static void setCommentsFromIssue(Issue issue, IssueResponse responseObject) {
        responseObject.comments = issue.getComments().stream()
                .map(comment -> CommentResponse.fromRowAndComment(issue, comment))
                .collect(Collectors.toSet());
    }
}
