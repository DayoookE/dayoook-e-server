package inha.dayoook_e.tutor.api.controller.dto.response;

import inha.dayoook_e.mapping.api.controller.dto.response.SearchDayResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchTimeSlotResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TutorScheduleData(

        SearchDayResponse day,
        SearchTimeSlotResponse timeSlot,

        @NotNull
        @Schema(description = "튜터 스케줄 가능 여부", example = "true")
        Boolean isAvailable
) {
}
