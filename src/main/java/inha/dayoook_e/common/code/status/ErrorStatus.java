package inha.dayoook_e.common.code.status;

import inha.dayoook_e.common.code.BaseErrorCode;
import inha.dayoook_e.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ErrorStatus는 서버 응답 시 사용되는 에러 코드를 정의.
 */
@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    /**
     * 400 : Request, Response 오류
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON4000", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON4001", "로그인 인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON4003", "접근 권한이 없는 요청입니다."),
    RESPONSE_ERROR(HttpStatus.NOT_FOUND, "COMMON4004", "값을 불러오는데 실패하였습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON4005", "입력값이 올바르지 않습니다."),
    DUPLICATION_REQUEST(HttpStatus.BAD_REQUEST, "COMMON4006", "중복된 요청입니다."),

    NOT_FIND_USER(HttpStatus.NOT_FOUND, "USER4000", "유저를 찾을 수 없습니다."),
    NOT_APPROVED_USER(HttpStatus.FORBIDDEN, "USER4001", "승인되지 않은 유저입니다."),
    INVALID_LANGUAGE_ID(HttpStatus.BAD_REQUEST, "LANGUAGE4000", "유효하지 않은 언어 ID입니다."),
    COUNTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "COUNTRY4000", "국가를 찾을 수 없습니다."),
    NOT_FIND_DAY(HttpStatus.NOT_FOUND, "DAY4000", "요일을 찾을 수 없습니다."),
    NOT_FIND_TIMESLOT(HttpStatus.NOT_FOUND, "TIMESLOT4000", "시간대를 찾을 수 없습니다."),

    INVALID_ROLE(HttpStatus.BAD_REQUEST, "ROLE4000", "잘못된 유저 권한입니다."),

    DAY_NOT_FOUND(HttpStatus.NOT_FOUND, "DAY4000", "요일을 찾을 수 없습니다."),
    TIME_SLOT_NOT_FOUND(HttpStatus.NOT_FOUND, "TIMESLOT4000", "시간대를 찾을 수 없습니다."),
    INVALID_DAY_ID(HttpStatus.BAD_REQUEST, "DAY4001", "유효하지 않은 요일 ID입니다."),
    INVALID_TIME_SLOT_ID(HttpStatus.BAD_REQUEST, "TIMESLOT4001", "유효하지 않은 시간대 ID입니다."),

    SONG_NOT_FOUND(HttpStatus.NOT_FOUND, "SONG4000", "동요를 찾을 수 없습니다."),
    SONG_ALREADY_COMPLETE(HttpStatus.BAD_REQUEST, "SONG4001", "이미 완료한 동요입니다."),

    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE4000", "스케줄을 찾을 수 없습니다."),
    SCHEDULE_ALREADY_BOOKED(HttpStatus.BAD_REQUEST, "SCHEDULE4001", "이미 예약된 스케줄입니다."),

    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLICATION4000", "신청 정보를 찾을 수 없습니다."),
    DUPLICATE_APPLICATION(HttpStatus.BAD_REQUEST, "APPLICATION4001", "이미 신청한 강의가 있습니다."),
    SCHEDULE_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "APPLICATION4002", "해당 시간에 수업이 불가능합니다."),
    INVALID_APPLICATION_STATUS(HttpStatus.BAD_REQUEST, "APPLICATION4003", "유효하지 처않은 신청 상태입니다."),
    APPLICATION_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLICATION4004", "신청 그룹을 찾을 수 없습니다."),
    DUPLICATE_TIMESLOT(HttpStatus.BAD_REQUEST, "APPLICATION4005", "중복된 시간대가 있습니다."),
    TIMESLOT_ALREADY_BOOKED(HttpStatus.BAD_REQUEST, "APPLICATION4006", "이미 예약된 시간대가 있습니다."),

    NOT_FIND_LESSON(HttpStatus.NOT_FOUND, "LESSON4000", "강의를 찾을 수 없습니다."),

    STORYBOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "STORYBOOK4000", "동화를 찾을 수 없습니다."),
    STORYBOOK_ALREADY_COMPLETE(HttpStatus.BAD_REQUEST, "STORYBOOK4001", "이미 완료한 동화입니다."),
    STORYBOOK_PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORYBOOK4002", "동화 페이지를 찾을 수 없습니다."),

    TUTORINFO_NOT_FOUND(HttpStatus.NOT_FOUND, "TUTORINFO4000", "튜터 정보를 찾을 수 없습니다."),

    LESSON_NOT_FOUND(HttpStatus.NOT_FOUND, "LESSON4000", "강의를 찾을 수 없습니다."),
    UNAUTHORIZED_TUTOR(HttpStatus.FORBIDDEN, "LESSON4001", "권한이 없는 튜터의 접근입니다."),
    TOO_EARLY_TO_CREATE_LESSON(HttpStatus.BAD_REQUEST, "LESSON4002", "강의 생성이 너무 이릅니다."),
    LESSON_SCHEDULE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "LESSON4003", "이미 생성된 강의 일정이 있습니다."),
    INVALID_DAY_NAME(HttpStatus.BAD_REQUEST, "LESSON4004", "유효하지 않은 요일 이름입니다."),
    LESSON_SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "LESSON4005", "강의 일정을 찾을 수 없습니다."),
    INVALID_LESSON_STATUS(HttpStatus.BAD_REQUEST, "LESSON4006", "유효하지 않은 강의 상태입니다."),
    LESSON_NOT_STARTED(HttpStatus.BAD_REQUEST, "LESSON4007", "수업이 시작되지 않았습니다."),
    LESSON_ALREADY_STARTED(HttpStatus.BAD_REQUEST, "LESSON4009", "이미 시작된 강의입니다."),
    MEETING_ROOM_CREATION_FAILED(HttpStatus.BAD_REQUEST, "LESSON4010", "회의실 생성에 실패하였습니다."),

    TRANSLATION_FAILED(HttpStatus.BAD_REQUEST, "PAPAGO4000", "번역에 실패하였습니다."),

    EMPTY_JWT(HttpStatus.UNAUTHORIZED, "JWT4000", "JWT를 입력해주세요"),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "JWT4001", "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(HttpStatus.FORBIDDEN, "JWT4002", "권한이 없는 유저의 접근입니다."),
    MISSING_AUTH_HEADER(HttpStatus.UNAUTHORIZED, "JWT4003","인증 헤더가 없습니다."),

    INVALID_PAGE(HttpStatus.BAD_REQUEST, "PAGE4000", "유효하지 않은 페이지입니다."),
    PAGE_COUNT_MISMATCH(HttpStatus.BAD_REQUEST, "PAGE4001", "페이지 수가 일치하지 않습니다."),

    //토큰 만료
    INVALID_JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT4004", "만료된 JWT입니다."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "JWT4005", "유효하지 않은 서명입니다."),

    S3_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "S34000", "S3 업로드에 실패하였습니다."),
    FILE_CONVERT_ERROR(HttpStatus.BAD_REQUEST, "FILE4000", "파일 변환에 실패하였습니다."),


    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "TOKEN4001", "토큰이 존재하지 않습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN4002,", "유효하지 않은 토큰입니다."),

    /**
     * 500 :  Database, Server 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5000", "서버 에러, 관리자에게 문의 바랍니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5001", "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5002", "서버와의 연결에 실패하였습니다."),
    PASSWORD_ENCRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5003", "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5004", "비밀번호 복호화에 실패하였습니다"),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5005", "예상치 못한 에러가 발생했습니다."),
    FAILED_TO_RECEIVE_FRAME(HttpStatus.INTERNAL_SERVER_ERROR, "FRAME4000", "프레임을 받는데 실패하였습니다.");



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    /**
     * 에러 메시지와 코드를 포함하는 ErrorReasonDTO를 반환.
     *
     * @return 에러 메시지와 코드가 포함된 ErrorReasonDTO
     */
    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    /**
     * HTTP 상태와 에러 메시지, 코드를 포함하는 ErrorReasonDTO를 반환.
     *
     * @return HTTP 상태와 에러 메시지, 코드가 포함된 ErrorReasonDTO
     */

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}