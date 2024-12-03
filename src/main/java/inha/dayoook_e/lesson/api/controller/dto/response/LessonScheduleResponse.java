package inha.dayoook_e.lesson.api.controller.dto.response;

import inha.dayoook_e.lesson.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LessonScheduleResponse(
        @NotNull
        @Schema(description = "수업 일정 ID", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "수업 상태", example = "SCHEDULED")
        Status status,

        @NotNull
        @Schema(description = "수업 시작 시간", example = "2021-10-01T10:00:00")
        LocalDateTime startAt,

        @NotNull
        @Schema(description = "화상회의 url", example = "google-meet.com/room/1")
        String roomUrl
) {
}
