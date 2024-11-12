package inha.dayoook_e.application.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ApplyRequest(
        @NotNull
        @Schema(description = "튜터 id", example = "1")
        Integer tutorId,
        @NotNull
        @Schema(description = "신청 요일 id", example = "1")
        Integer dayId,

        @NotNull
        @Schema(description = "신청 시간대 id", example = "1")
        Integer timeSlotId,

        @Schema(description = "튜터에게 보낼 메시지", example = "저는 노래를 통해 배우는 것을 선호합니다.")
        String message
) {
}

