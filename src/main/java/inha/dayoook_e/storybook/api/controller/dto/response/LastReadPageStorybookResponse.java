package inha.dayoook_e.storybook.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record LastReadPageStorybookResponse(

        @NotNull
        @Schema(description = "동화 ID")
        Integer id,
        @NotNull
        @Schema(description = "마지막 읽은 페이지 번호")
        Integer lastPageNumber
) {
}
