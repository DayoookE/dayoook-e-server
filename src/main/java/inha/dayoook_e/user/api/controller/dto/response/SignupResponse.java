package inha.dayoook_e.user.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SignupResponse(
        @NotNull
        @Schema(description = "유저 아이디", example = "1")
        Integer id
) {
}
