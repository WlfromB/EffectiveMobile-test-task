package org.example.effectivemobiletesttask.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.effectivemobiletesttask.entities.Comment;

@Data
@Schema(description = "Схема запроса на добавление комментария к записи.")
public class CommentCreateRequest {
    @NotBlank
    @Schema(description = "Название задачи.", example = "Название задачи.")
    private String rowTitle;
    @NotBlank(message = "Не должен быть пустым. Максимум 150 символов.")
    @Schema(description = "Текст комментария.", example = "Положительный комментарий!)")
    private String comment;
    
    public Comment from(){
        Comment comment = new Comment();
        comment.setText(this.comment);
        return comment;
    }
}
