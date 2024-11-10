package inha.dayoook_e.song.api.controller.dto.response;

import inha.dayoook_e.mapping.api.dto.response.SearchCountryResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SongSearchResponse(
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
        @Schema(description = "미디어 url", example = "/song/media/1/4f937d69-9fe6-4fe7-a761-f171a8212274")
        String mediaUrl,

        @NotNull
        @Schema(description = "가사", example = "나는 코딱지 콧구멍 안에 살지\n" +
                "딱지 딱지 코딱지 \n" +
                "\n" +
                "콧털과 점액 세균 잡고 만들어져 \n" +
                "딱지 딱지 코딱지 \n" +
                "\n" +
                "세균 세균 잡아 먼지 먼지 잡아\n" +
                "우리가 탄생하지\n" +
                "후비 바비 띠용 띠용 \n" +
                "\n" +
                "코 코 딱지 딱지\n" +
                "코 코 딱지 딱지\n" +
                "우리는 코딱지 ")
        String lyrics,

        SearchCountryResponse country,

        @NotNull
        @Schema(description = "좋아요 누른 여부", example = "true")
        Boolean liked,

        @NotNull
        @Schema(description = "완료 여부", example = "true")
        Boolean isCompleted
) {
}
