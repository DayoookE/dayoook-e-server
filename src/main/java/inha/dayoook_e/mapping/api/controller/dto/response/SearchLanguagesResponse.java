package inha.dayoook_e.mapping.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SearchLanguagesResponse(
        @NotNull
        @Schema(description = "언어 id", example = "1")
        Integer id,
        @NotNull
        @Schema(description = "언어 이름", example = "한국")
        String name

) {
}
