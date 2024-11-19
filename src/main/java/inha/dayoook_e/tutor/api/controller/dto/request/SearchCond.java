package inha.dayoook_e.tutor.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SearchCond(
        @Schema(description = "가능 언어 id", example = "[1, 2]")
        List<Integer> languageId,

        @Schema(description = "연령층 id", example = "[1, 2]")
        List<Integer> ageGroupId,

        @Schema(description = "이름", example = "김OO")
        String name
        ) {
}