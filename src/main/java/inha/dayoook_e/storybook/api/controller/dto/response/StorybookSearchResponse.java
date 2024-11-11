package inha.dayoook_e.storybook.api.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record StorybookSearchResponse(
        @NotNull
        @Schema(description = "동화 id", example = "1")
        Integer id,
        @NotNull
        @Schema(description = "동화 제목", example = "토끼와 거북이")
        String title,

        @NotNull
        @Schema(description = "동화 설명", example = "토끼와 거북이 동화입니다.")
        String description,

        @NotNull
        @Schema(description = "썸네일 url", example = "/storybook/thumbnail/1/4f937d69-9fe6-4fe7-a761-f171a8212274")
        String thumbnailUrl,

        @NotNull
        @Schema(description = "동화 그림 url", example = "/storybook/page/1/4f937d69-9fe6-4fe7-a761-f171a8212274")
        String pageUrl,

        @NotNull
        @Schema(description = "페이지 번호", example = "1")
        Integer pageNumber,

        @NotNull
        @Schema(description = "동화 내용", example = "옛날 옛적에 토끼와 거북이가 살았습니다.")
        String content,

        @NotNull
        @Schema(description = "동화의 페이지 수", example = "5")
        Integer pageCount
) {
}
