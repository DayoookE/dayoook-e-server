package inha.dayoook_e.user.api.controller.dto.request;

import inha.dayoook_e.common.validation.annotation.EmailUnique;
import inha.dayoook_e.common.validation.annotation.ValidEmail;
import inha.dayoook_e.common.validation.annotation.ValidParameter;
import inha.dayoook_e.common.validation.annotation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TutorSignupRequest(
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
        @Schema(description = "자기 소개", example = "안녕하세요")
        String introduction,

        @NotNull
        @Schema(description = "직업", example = "한국어강사")
        String occupation,


        @Schema(description = "경력", example = """
            ["인하대학교 국어교육과 학사", "인하대학교 다문화교육과 석사", "인하대학교 국어교육과 교수"]
            """)
        List<String> descriptionList

) {
}
