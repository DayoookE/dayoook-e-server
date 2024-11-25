package inha.dayoook_e.tutor.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record TutorScheduleRequest(

        @NotEmpty
        @Schema(description = "튜터 가능 시간 목록")
        List<ScheduleTimeSlot> scheduleTimeSlots

) {}