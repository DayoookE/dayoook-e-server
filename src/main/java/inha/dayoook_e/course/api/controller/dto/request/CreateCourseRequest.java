package inha.dayoook_e.course.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateCourseRequest(
        @NotNull
        @Schema(description = "튜터 id", example = "1")
        Integer tutorId,

        @NotNull
        @Schema(description = "튜티 id", example = "1")
        Integer tuteeId,

        @NotNull
        @Schema(description = "신청 요일 id", example = "1")
        Integer dayId,

        @NotNull
        @Schema(description = "신청 시간대 id", example = "1")
        Integer timeSlotId
) {
}
