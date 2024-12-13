package inha.dayoook_e.auth.api.controller.dto.response;

import inha.dayoook_e.user.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * LoginResponse는 로그인 응답 정보를 담는 DTO 클래스.
 */
public record LoginResponse(
        @NotNull
        @Schema(description = "유저 아이디", example = "1")
        Integer id,
        @NotNull
        @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImV4cCI6MTYzNzIwNjIwM30.1J9")
        String accessToken,

        @NotNull
        @Schema(description = "유저 권한", example = "TUTEE")
        Role role
) {
}
