package inha.dayoook_e.tutee.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TuteeScheduleResponse(

        @NotNull
        @Schema(description = "년도", example = "2024")
        int year,

        @NotNull
        @Schema(description = "월", example = "12")
        int month,

        @Schema(description = "수업 일정 목록")
        List<DaySchedule> schedules
) {}
