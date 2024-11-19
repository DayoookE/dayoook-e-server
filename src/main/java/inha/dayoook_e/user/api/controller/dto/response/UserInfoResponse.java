package inha.dayoook_e.user.api.controller.dto.response;

import inha.dayoook_e.user.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UserInfoResponse(
        @NotNull
        @Schema(description = "유저 ID", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "유저 역할", example = "TUTOR")
        Role role
) {
}
