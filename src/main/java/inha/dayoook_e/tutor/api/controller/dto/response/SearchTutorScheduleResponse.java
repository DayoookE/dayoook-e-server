package inha.dayoook_e.tutor.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SearchTutorScheduleResponse(

        @NotNull
        @Schema(description = "튜터 ID", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "튜터 이름", example = "홍길동")
        String name,

        List<TutorScheduleData> tutorScheduleDataList
) {
}
