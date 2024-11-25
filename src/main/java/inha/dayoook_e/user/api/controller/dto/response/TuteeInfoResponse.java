package inha.dayoook_e.user.api.controller.dto.response;

import inha.dayoook_e.tutee.domain.enums.Level;
import inha.dayoook_e.user.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TuteeInfoResponse(

        @NotNull
        @Schema(description = "튜티 ID", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "유저 역할", example = "TUTEE")
        Role role,

        @NotNull
        @Schema(description = "튜티 이름", example = "김튜티")
        String name,

        @NotNull
        @Schema(description = "프로필 이미지 URL", example = "https://www.naver.com")
        String profileUrl,

        @NotNull
        @Schema(description = "성별", example = "true")
        Boolean gender,

        @NotNull
        @Schema(description = "나이", example = "20")
        Integer age,

        @NotNull
        @Schema(description = "포인트", example = "100")
        Integer point,

        @NotNull
        @Schema(description = "레벨", example = "SEEDLING")
        Level level

) implements UserInfoResponse {
}
