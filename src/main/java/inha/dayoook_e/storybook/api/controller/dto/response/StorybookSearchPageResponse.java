package inha.dayoook_e.storybook.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record StorybookSearchPageResponse(
        @NotNull
        @Schema(description = "동화 id", example = "1")
        Integer id,
        @NotNull
        @Schema(description = "동화 제목", example = "토끼와 거북이")
        String title,

        @NotNull
        @Schema(description = "설명", example = "토끼와 거북이 동화입니다.")
        String description,

        @NotNull
        @Schema(description = "썸네일 url", example = "/song/thumbnail/1/4f937d69-9fe6-4fe7-a761-f171a8212274")
        String thumbnailUrl,

        @NotNull
        @Schema(description = "좋아요 누른 여부", example = "true")
        Boolean liked,

        @NotNull
        @Schema(description = "완료 여부", example = "true")
        Boolean isCompleted,

        @NotNull
        @Schema(description = "총 페이지 수", example = "5")
        Integer totalPageNumber,

        @NotNull
        @Schema(description = "마지막으로 읽은 페이지 번호", example = "1")
        Integer pageCount
) {
}
