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
    USER_INFO_OK(HttpStatus.OK, "USER2000", "유저 정보 조회 성공"),

    SONG_CREATE_OK(HttpStatus.CREATED, "SONG2000", "동요 생성 성공"),
    SONG_SEARCH_PAGE_OK(HttpStatus.OK, "SONG2001", "동요 조건 검색 성공"),
    SONG_SEARCH_OK(HttpStatus.OK, "SONG2002", "동요 상세 조회 성공"),
    SONG_TOGGLE_LIKE_OK(HttpStatus.OK, "SONG2003", "동요 좋아요 토글 성공"),
    SONG_COMPLETE_OK(HttpStatus.OK, "SONG2004", "동요 완료 성공"),

    TUTOR_SEARCH_OK(HttpStatus.OK, "TUTOR2000", "튜터 상세 조회 성공"),
    TUTOR_SEARCH_PAGE_OK(HttpStatus.OK, "TUTOR2001", "튜터 조건 검색 성공"),
    TUTOR_SCHEDULE_CREATE_OK(HttpStatus.CREATED, "TUTOR2002", "튜터 일정 생성 성공"),
    TUTOR_SCHEDULE_SEARCH_OK(HttpStatus.OK, "TUTOR2003", "튜터 일정 조회 성공"),
    TUTOR_APPLICATION_SEARCH_OK(HttpStatus.OK, "TUTOR2004", "튜터 신청 목록 조회 성공"),

    APPLICATION_CREATE_OK(HttpStatus.CREATED, "APPLICATION2000", "신청 생성 성공"),
    APPLICATION_APPROVE_OK(HttpStatus.CREATED, "APPLICATION2001", "신청 승인 성공"),
    APPLICATION_REJECT_OK(HttpStatus.CREATED, "APPLICATION2002", "신청 거절 성공"),
    APPLICATION_CANCEL_OK(HttpStatus.CREATED, "APPLICATION2003", "신청 취소 성공"),


    STORYBOOK_CREATE_OK(HttpStatus.CREATED, "STORYBOOK2000", "동화 생성 성공"),
    STORYBOOK_TOGGLE_LIKE_OK(HttpStatus.OK, "STORYBOOK2001", "동화 좋아요 토글 성공"),
    STORYBOOK_COMPLETE_OK(HttpStatus.OK, "STORYBOOK2002", "동화 완료 성공"),
    STORYBOOK_SEARCH_PAGE_OK(HttpStatus.OK, "STORYBOOK2003", "동화 조건 검색 성공"),
    STORYBOOK_SEARCH_OK(HttpStatus.OK, "STORYBOOK2004", "동화 상세 조회 성공"),
    STORYBOOK_UPDATE_LAST_READ_PAGE_OK(HttpStatus.OK, "STORYBOOK2005", "동화 마지막 읽은 페이지 업데이트 성공"),

    LANGUAGES_SEARCH_OK(HttpStatus.OK, "MAPPING2000", "언어 목록 조회 성공"),
    COUNTRIES_SEARCH_OK(HttpStatus.OK, "MAPPING2001", "국가 목록 조회 성공"),
    AGE_GROUPS_SEARCH_OK(HttpStatus.OK, "MAPPING2002", "연령대 목록 조회 성공"),
    DAYS_SEARCH_OK(HttpStatus.OK, "MAPPING2003", "요일 목록 조회 성공"),
    TIME_SLOTS_SEARCH_OK(HttpStatus.OK, "MAPPING2004", "시간대 목록 조회 성공");

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