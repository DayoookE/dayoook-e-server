package inha.dayoook_e.song.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SongSearchPageResponse(
        @NotNull
        @Schema(description = "동요 id", example = "1")
        Integer id,
        @NotNull
        @Schema(description = "제목", example = "코딱지 송")
        String title,

        @NotNull
        @Schema(description = "설명", example = "한국 창작 동요")
        String description,

        @NotNull
        @Schema(description = "썸네일 url", example = "/song/thumbnail/1/4f937d69-9fe6-4fe7-a761-f171a8212274")
        String thumbnailUrl,

        @NotNull
        @Schema(description = "좋아요 누른 여부", example = "true")
        Boolean liked,

        @NotNull
        @Schema(description = "완료 여부", example = "true")
        Boolean isCompleted
) {
}
