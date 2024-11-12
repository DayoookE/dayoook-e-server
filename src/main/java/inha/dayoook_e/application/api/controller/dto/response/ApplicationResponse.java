package inha.dayoook_e.application.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ApplicationResponse (
    @NotNull
    @Schema(description = "신청 id", example = "1")
    Integer id
) {}
