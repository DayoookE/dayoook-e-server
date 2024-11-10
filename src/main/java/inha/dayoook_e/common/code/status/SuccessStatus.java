package inha.dayoook_e.common.code.status;

import inha.dayoook_e.common.code.BaseCode;
import inha.dayoook_e.common.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    /**
     * 일반적인 응답
     * */
    OK(HttpStatus.OK, "COMMON2000", "성공입니다."),

    LOGIN_OK(HttpStatus.OK, "AUTH2000", "로그인 성공"),

    TUTEE_SIGNUP_OK(HttpStatus.CREATED, "USER2010", "튜티 회원가입 성공"),
    TUTOR_SIGNUP_OK(HttpStatus.CREATED, "USER2011", "튜터 회원가입 성공"),

    SONG_CREATE_OK(HttpStatus.CREATED, "SONG2000", "동요 생성 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}