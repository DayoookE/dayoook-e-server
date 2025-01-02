package inha.dayoook_e.mapping.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SearchTimeSlotResponse(
    @NotNull
    @Schema(description = "시간대 ID", example = "1")
    Integer id,

    @NotNull
    @Schema(description = "시간대", example = "오전 10시")
    String time
) {
}
