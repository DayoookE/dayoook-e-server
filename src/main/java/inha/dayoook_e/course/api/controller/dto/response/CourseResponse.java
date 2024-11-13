package inha.dayoook_e.course.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CourseResponse (
        @NotNull
        @Schema(description = "강의 id", example = "1")
        Integer id
) {}
