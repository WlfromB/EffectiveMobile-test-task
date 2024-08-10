package org.example.effectivemobiletesttask.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "Параметры пагинации.")
public class PaginationParams {
    @Min(0)
    @Schema(description = "Номер страницы", example = "0")
    private int page = 0;

    @Min(1)
    @Max(100)
    @Schema(description = "Размер страницы.", example = "10")
    private int size = 10;

    @Schema(description = "Поле сортировки.", example = "id")
    private String sortBy = "id";
}
