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

    SONG_CREATE_OK(HttpStatus.CREATED, "SONG2000", "동요 생성 성공"),
    SONG_SEARCH_PAGE_OK(HttpStatus.OK, "SONG2001", "동요 조건 검색 성공"),
    SONG_SEARCH_OK(HttpStatus.OK, "SONG2002", "동요 상세 조회 성공"),
    SONG_TOGGLE_LIKE_OK(HttpStatus.OK, "SONG2003", "동요 좋아요 토글 성공"),
    SONG_COMPLETE_OK(HttpStatus.OK, "SONG2004", "동요 완료 성공");

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