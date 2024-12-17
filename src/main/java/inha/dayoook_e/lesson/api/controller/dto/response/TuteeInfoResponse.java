package inha.dayoook_e.lesson.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TuteeInfoResponse(

        @NotNull
        @Schema(description = "튜티 ID", example = "1")
        Integer tuteeId,

        @NotNull
        @Schema(description = "튜티 이름", example = "김튜티")
        String name

) {
}
