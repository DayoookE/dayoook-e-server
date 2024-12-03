package inha.dayoook_e.user.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpcomingLessonInfo(

        @NotNull
        @Schema(description = "튜터 ID", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "튜터 이름", example = "김튜터")
        String name,

        @NotNull
        @Schema(description = "강의 시작 시간", example = "2024-12-03T15:00:00")
        LocalDateTime startAt
) {
}
