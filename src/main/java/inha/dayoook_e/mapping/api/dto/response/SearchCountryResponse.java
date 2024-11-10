package inha.dayoook_e.mapping.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SearchCountryResponse(
        @NotNull
        @Schema(description = "국가 id", example = "1")
        Integer id,
        @NotNull
        @Schema(description = "국가 이름", example = "한국")
        String name,

        @NotNull
        @Schema(description = "국기 url", example = "/country/flag/1/4f937d69-9fe6-4fe7-a761-f171a8212274")
        String flagUrl
) {
}
