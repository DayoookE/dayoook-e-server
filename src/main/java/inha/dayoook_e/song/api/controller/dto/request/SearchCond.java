package inha.dayoook_e.song.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SearchCond(
        @Schema(description = "국가 id", example = "1")
        Integer countryId,
        @Schema(description = "좋아요 여부", example = "true")
        Boolean liked,
        @Schema(description = "제목", example = "코딱지 송")
        String title
) {
}
