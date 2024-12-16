package inha.dayoook_e.papago.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PapagoResponse(

        @NotNull
        @Schema(description = "번역된 텍스트", example = "Hello")
        String translatedText
) {
}
