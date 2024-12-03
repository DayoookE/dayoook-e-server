package inha.dayoook_e.lesson.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateLessonRequest(

        @NotNull
        @Schema(description = "신청 그룹 ID", example = "1")
        Integer id

) {
}
