package inha.dayoook_e.application.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TimeSlotRequest(

        @NotNull
        @Schema(description = "신청 요일 id", example = "1")
        Integer dayId,

        @NotNull
        @Schema(description = "신청 시간대 id", example = "1")
        Integer timeSlotId
) {
}
