package org.example.effectivemobiletesttask.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.example.effectivemobiletesttask.entities.Comment;
import org.example.effectivemobiletesttask.entities.Issue;

@Getter
@Schema(description = "Вид ответа API для запросов комментариев.")
public class CommentResponse {
    @Schema(description = "Название задания.", example = "Название задачи.")
    private String rowTitle;
    @Schema(description = "Текст комментария.", example = "Положительный комментарий!)")
    private String comment;

    public CommentResponse() {}

    public static CommentResponse fromRowAndComment(Issue issue, Comment comment) {
        CommentResponse responseObject = new CommentResponse();
        responseObject.setRowTitle(issue);
        responseObject.setComment(comment);
        return responseObject;
    }

    public void setRowTitle(Issue issue) {
        this.rowTitle = issue.getTitle();
    }

    public void setComment(Comment comment) {
        this.comment = comment.getText();
    }
}

