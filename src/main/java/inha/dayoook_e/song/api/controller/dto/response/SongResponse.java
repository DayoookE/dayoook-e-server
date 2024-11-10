package inha.dayoook_e.song.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SongResponse(
        @NotNull
        @Schema(description = "동요 id", example = "1")
        Integer id
) {
}
