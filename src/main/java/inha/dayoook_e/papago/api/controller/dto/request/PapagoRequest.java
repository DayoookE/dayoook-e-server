package inha.dayoook_e.papago.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PapagoRequest(

        @NotNull
        @Schema(description = "원본 텍스트 언어", example = "ko")
        String source,

        @NotNull
        @Schema(description = "번역할 텍스트 언어", example = "en")
        String target,

        @NotNull
        @Schema(description = "번역할 텍스트", example = "안녕하세요")
        String text
) {}
