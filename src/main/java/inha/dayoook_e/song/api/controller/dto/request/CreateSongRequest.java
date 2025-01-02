package inha.dayoook_e.song.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateSongRequest(
        @NotNull
        @Schema(description = "제목", example = "코딱지 송")
        String title,
        @NotNull
        @Schema(description = "설명", example = "한국 창작 동요")
        String description,

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
        @NotNull
        @Schema(description = "국가 id", example = "1")
        Integer countryId
) {
}
