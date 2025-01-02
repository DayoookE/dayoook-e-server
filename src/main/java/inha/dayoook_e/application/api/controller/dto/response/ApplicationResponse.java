package inha.dayoook_e.application.api.controller.dto.response;

import inha.dayoook_e.application.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record ApplicationResponse (
    @NotNull
    @Schema(description = "신청 id", example = "1")
    Integer id,

    @NotNull
    @Schema(description = "신청 상태", example = "APPLYING")
    @Enumerated(EnumType.STRING)
    Status status
) {}
