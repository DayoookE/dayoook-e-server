package inha.dayoook_e.tutor.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TutorScheduleRequest(
        @NotNull
        @Schema(description = "요일 ID 리스트", example = "[1, 2, 3]")
        List<Integer> dayIds,

        @NotNull
        @Schema(description = "시간대 ID 리스트", example = "[1, 2, 3]")
        List<Integer> timeSlotIds
) {}