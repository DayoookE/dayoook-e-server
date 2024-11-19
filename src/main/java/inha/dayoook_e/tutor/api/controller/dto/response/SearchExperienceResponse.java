package inha.dayoook_e.tutor.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SearchExperienceResponse (

        @NotNull
        @Schema(description = "경력 ID", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "경력 설명", example = "인하대학교 국어교육과 학사")
        String description

)
{}
