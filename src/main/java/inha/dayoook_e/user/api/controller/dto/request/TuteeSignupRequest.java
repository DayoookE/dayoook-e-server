package inha.dayoook_e.user.api.controller.dto.request;

import inha.dayoook_e.common.validation.annotation.EmailUnique;
import inha.dayoook_e.common.validation.annotation.ValidEmail;
import inha.dayoook_e.common.validation.annotation.ValidParameter;
import inha.dayoook_e.common.validation.annotation.ValidPassword;
import inha.dayoook_e.user.domain.enums.KoreanLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TuteeSignupRequest(
        @NotNull
        @EmailUnique
        @Email
        @ValidEmail
        @ValidParameter
        @Schema(description = "유저 이메일", example = "ghkdrbgur13@inha.edu")
        String email,
        @NotEmpty
        @ValidParameter
        @ValidPassword
        @Schema(description = "비밀번호", example = "password2@")
        String password,
        @NotEmpty
        @ValidParameter
        @Schema(description = "이름", example = "황규혁")
        String name,

        @NotNull
        @Schema(description = "나이", example = "26")
        Integer age,

        @NotNull
        @Schema(description = "성별", example = "true (남성), false (여성)")
        Boolean gender,

        @Schema(description = "사용 가능한 언어", example = "[1, 2, 3]")
        List<Integer> languageIdList,

        @NotNull
        @Schema(description = "한국어 수준", example = "BEGINNER")
        KoreanLevel koreanLevel


) {
}
