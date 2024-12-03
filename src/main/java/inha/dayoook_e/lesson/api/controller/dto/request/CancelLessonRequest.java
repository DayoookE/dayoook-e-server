package inha.dayoook_e.lesson.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CancelLessonRequest(



        @NotNull
        @Schema(description = "취소 사유", example = "학생 불참")
        String reason
) {
}
