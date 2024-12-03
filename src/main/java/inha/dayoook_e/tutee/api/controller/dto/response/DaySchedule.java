package inha.dayoook_e.tutee.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record DaySchedule(

        @Schema(description = "일", example = "15")
        int day,

        @Schema(description = "수업 목록")
        List<LessonInfo> lessons
) {
}
