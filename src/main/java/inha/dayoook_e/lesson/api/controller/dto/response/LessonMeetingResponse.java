package inha.dayoook_e.lesson.api.controller.dto.response;

import inha.dayoook_e.lesson.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LessonMeetingResponse(

        @NotNull
        @Schema(description = "레슨 id", example = "1")
        Integer id,

        @Schema(description = "구글미트 url", example = "https://meet.google.com/abc-def-ghi-jkl")
        String meetingUri,

        @Schema(description = "레슨 생성일", example = "2021-08-01T00:00:00")
        LocalDateTime createdAt,

        @Schema(description = "레슨 상태", example = "SCHEDULED")
        Status status


) {
}
