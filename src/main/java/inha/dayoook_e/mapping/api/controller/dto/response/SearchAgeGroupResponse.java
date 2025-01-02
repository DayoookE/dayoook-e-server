package inha.dayoook_e.mapping.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SearchAgeGroupResponse(
    @NotNull
    @Schema(description = "연령대 ID", example = "1")
    Integer id,

    @NotNull
    @Schema(description = "연령대 이름", example = "10대")
    String name
) {
}
