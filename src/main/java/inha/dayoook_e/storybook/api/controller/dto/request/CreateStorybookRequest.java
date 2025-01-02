package inha.dayoook_e.storybook.api.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateStorybookRequest(
        @NotNull
        @Schema(
                description = "제목",
                example = "토끼와 거북이"
        )
        String title,

        @NotNull
        @Schema(
                description = "설명",
                example = "토끼와 거북이의 경주 이야기"
        )
        String description,

        @NotNull
        @Schema(
                description = "국가 id",
                example = "1"
        )
        Integer countryId,

        @NotNull
        @Size(min = 1)
        @Schema(
                description = "페이지 내용 리스트",
                example = """
            [
              {
                "pageNumber": 1,
                "content": "옛날 옛적에 토끼와 거북이가 살았습니다."
              },
              {
                "pageNumber": 2,
                "content": "어느 날, 토끼가 거북이를 보고 경주를 하자고 놀렸습니다."
              },
              {
                "pageNumber": 3,
                "content": "거북이는 천천히라도 끝까지 포기하지 않겠다고 대답했습니다."
              },
              {
                "pageNumber": 4,
                "content": "경주가 시작되자 토끼는 빠르게 앞으로 나갔고, 거북이는 천천히 걸어갔습니다."
              },
              {
                "pageNumber": 5,
                "content": "그러나 토끼가 쉬는 사이 거북이는 결승점에 먼저 도착해 승리했습니다."
              }
            ]
            """
        )
        List<PageContent> pageContents
) {
        public record PageContent(
                @NotNull
                @Schema(
                        description = "페이지 번호",
                        example = "1"
                )
                Integer pageNumber,

                @NotNull
                @Schema(
                        description = "페이지 내용",
                        example = "옛날 옛적에 토끼와 거북이가 살았습니다."
                )
                String content
        ) {}
}