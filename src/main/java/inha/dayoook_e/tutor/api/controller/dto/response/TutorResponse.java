package inha.dayoook_e.tutor.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TutorResponse(

        @NotNull
        @Schema(description = "튜터 ID", example = "1")
        Integer id
) {
}
