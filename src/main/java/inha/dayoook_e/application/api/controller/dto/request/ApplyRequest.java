package inha.dayoook_e.application.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ApplyRequest(
        @NotNull
        @Schema(description = "튜터 id", example = "1")
        Integer tutorId,
        @NotEmpty
        @Schema(description = "수업 시간 신청 목록")
        List<TimeSlotRequest> timeSlots,
        @Schema(description = "튜터에게 보낼 메시지", example = "저는 노래를 통해 배우는 것을 선호합니다.")
        String message
) {
}

