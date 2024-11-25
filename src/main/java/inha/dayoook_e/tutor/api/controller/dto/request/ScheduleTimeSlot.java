package inha.dayoook_e.tutor.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ScheduleTimeSlot(

        @NotNull
        @Schema(description = "요일 id", example = "1")
        Integer dayId,

        @NotNull
        @Schema(description = "시간대 id", example = "1")
        Integer timeSlotId
)
{
}
