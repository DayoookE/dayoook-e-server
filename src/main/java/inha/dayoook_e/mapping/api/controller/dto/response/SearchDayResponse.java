package inha.dayoook_e.mapping.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SearchDayResponse(
        @NotNull
        @Schema(description = "요일 id", example = "1")
        Integer id,
        @NotNull
        @Schema(description = "요일 이름", example = "한국")
        String name

) {
}
