package inha.dayoook_e.tutee.api.controller.dto.response;

import inha.dayoook_e.lesson.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record LessonInfo(

        @NotNull
        @Schema(description = "수업 ID", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "수업 시작 시간", example = "14:00")
        String startTime,

        @NotNull
        @Schema(description = "튜터 ID", example = "1")
        Integer tutorId,

        @NotNull
        @Schema(description = "튜터 이름", example = "김튜터")
        String tutorName,

        @Schema(description = "수업 상태", example = "SCHEDULED")
        Status status
) {}