package inha.dayoook_e.tutor.api.controller.dto.response;

import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;
import inha.dayoook_e.tutor.api.controller.dto.request.ScheduleTimeSlot;
import inha.dayoook_e.user.api.controller.dto.response.TuteeInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record SearchTutorApplicationResponse(

        @NotNull
        @Schema(description = "신청 그룹 id", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "튜티 정보")
        TuteeInfoResponse tuteeInfo,

        @NotEmpty
        @Schema(description = "튜티 사용 가능 언어")
        List<SearchLanguagesResponse>  languages,


        @NotEmpty
        @Schema(description = "신청 시간 목록")
        List<ScheduleTimeSlot> scheduleTimeSlots,

        @NotNull
        @Schema(description = "신청 시간", example = "2024-08-01T00:00:00")
        LocalDateTime createdAt,

        @NotNull
        @Schema(description = "신청 상태", example = "APPLYING")
        Status status,

        @NotNull
        @Schema(description = "메시지", example = "안녕하세요")
        String message

) {
}
