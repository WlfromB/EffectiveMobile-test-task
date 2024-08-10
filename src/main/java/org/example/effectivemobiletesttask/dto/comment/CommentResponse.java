package org.example.effectivemobiletesttask.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Вид ответа API для запросов комментариев.")
public class CommentResponse {
    @Schema(description = "Название задания.", example = "Название задачи.")
    private String rowTitle;
    @Schema(description = "Текст комментария.", example = "Положительный комментарий!)")
    private String comment;

    public CommentResponse(String title, String comment) {
        this.rowTitle = title;
        this.comment = comment;
    }
}

