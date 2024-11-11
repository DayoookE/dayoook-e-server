package inha.dayoook_e.storybook.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record LikedTuteeStorybookProgressResponse(
        @NotNull
        @Schema(description = "동화 id", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "좋아요 여부", example = "true")
        Boolean liked
) {
}
