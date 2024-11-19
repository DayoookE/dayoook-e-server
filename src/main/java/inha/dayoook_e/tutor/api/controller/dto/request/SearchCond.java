package inha.dayoook_e.tutor.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SearchCond(
        @Schema(description = "가능 언어 id", example = "1")
        Integer languageId,
        @Schema(description = "연령층 id", example = "1")
        Integer ageGroupId
) {
}