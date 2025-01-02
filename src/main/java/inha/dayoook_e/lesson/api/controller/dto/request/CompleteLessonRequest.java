package inha.dayoook_e.lesson.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CompleteLessonRequest(

        @NotNull
        @Schema(description = "수업 일정 ID", example = "1")
        Integer id

) {
}
