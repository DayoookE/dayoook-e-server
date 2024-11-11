package inha.dayoook_e.storybook.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record StorybookResponse(

        @NotNull
        @Schema(description = "동화 id", example = "1")
        Integer id
) {
}
