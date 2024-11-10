package inha.dayoook_e.auth.api.controller.dto.request;

import inha.dayoook_e.common.validation.annotation.ValidEmail;
import inha.dayoook_e.common.validation.annotation.ValidParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * LoginRequest는 로그인 요청 정보를 담는 DTO 클래스.
 */
public record LoginRequest(
        @NotNull
        @Email
        @ValidEmail
        @ValidParameter
        @Schema(description = "유저 이메일", example = "ghkdrbgur13@inha.edu")
        String email,
        @NotNull
        @ValidParameter
        @Schema(description = "비밀번호", example = "password2@")
        String password) {
}
