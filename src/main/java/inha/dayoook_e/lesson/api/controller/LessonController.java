package inha.dayoook_e.lesson.api.controller;

import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.lesson.api.controller.dto.request.CancelLessonRequest;
import inha.dayoook_e.lesson.api.controller.dto.request.CompleteLessonRequest;
import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonScheduleRequest;
import inha.dayoook_e.lesson.api.controller.dto.request.LessonSchedulesRequest;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonScheduleResponse;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonSchedulesResponse;
import inha.dayoook_e.lesson.api.service.LessonService;
import inha.dayoook_e.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static inha.dayoook_e.common.code.status.SuccessStatus.*;


/**
 * LessonController는 교육 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "lesson controller", description = "교육 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {

    private final LessonService lessonService;


    /**
     * 강의 일정 조회
     *
     * @param user 현재 로그인한 사용자
     * @param lessonSchedulesRequest 강의 일정 조회 요청
     * @return List<LessonSchedulesResponse>
     */
    @PostMapping
    @Operation(summary = "강의 일정 조회 API", description = "튜터의 강의 일정을 조회합니다.")
    public BaseResponse<List<LessonSchedulesResponse>> getLessonSchedules(@AuthenticationPrincipal User user,
                                                                          @Validated @RequestBody LessonSchedulesRequest lessonSchedulesRequest) {
        return BaseResponse.of(LESSON_SCHEDULE_GET_OK, lessonService.getLessonSchedules(user, lessonSchedulesRequest));
    }

    /**
     * 강의 일정 생성
     *
     * @param user 현재 로그인한 사용자
     * @param createLessonScheduleRequest 강의 일정 생성 요청
     * @return LessonScheduleResponse
     */
    @PostMapping("/schedules")
    @Operation(summary = "강의 일정 생성 API", description = "튜터가 강의 일정을 생성합니다.")
    public BaseResponse<LessonScheduleResponse> createLessonSchedule(
            @AuthenticationPrincipal User user,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String authToken,  // 헤더에서 토큰 추출
            @Validated @RequestBody CreateLessonScheduleRequest createLessonScheduleRequest) {

        log.info("강의 일정 생성 요청: {} {}", user.getName(), createLessonScheduleRequest);
        return BaseResponse.of(LESSON_SCHEDULE_CREATE_OK,
                lessonService.createLessonSchedule(user, authToken, createLessonScheduleRequest));
    }

    /**
     * 강의 완료 처리
     *
     * @param user 현재 로그인한 사용자
     * @param scheduleId 강의 일정 ID
     * @param completeLessonRequest 강의 완료 처리 요청
     * @return LessonScheduleResponse
     */
    @PatchMapping("/schedules/{scheduleId}/complete")
    @Operation(summary = "강의 완료 처리 API", description = "튜터가 강의를 완료 처리합니다.")
    public BaseResponse<LessonScheduleResponse> completeLessonSchedule(@AuthenticationPrincipal User user,
                                                                       @PathVariable("scheduleId") Integer scheduleId,
                                                                       @Validated @RequestBody CompleteLessonRequest completeLessonRequest) {
        log.info("강의 완료 처리 요청: {} {}", user.getName(), scheduleId);
        return BaseResponse.of(LESSON_SCHEDULE_COMPLETE_OK, lessonService.completeLessonSchedule(user, scheduleId, completeLessonRequest)
        );
    }

    /**
     * 강의 취소 처리
     *
     * @param user 현재 로그인한 사용자
     * @param scheduleId 강의 일정 ID
     * @param cancelLessonRequest 강의 취소 처리 요청
     * @return LessonScheduleResponse
     */
    @PatchMapping("/schedules/{scheduleId}/cancel")
    @Operation(summary = "강의 취소 처리 API", description = "튜터가 강의를 취소 처리합니다.")
    public BaseResponse<LessonScheduleResponse> cancelLessonSchedule(@AuthenticationPrincipal User user,
                                                                     @PathVariable("scheduleId") Integer scheduleId,
                                                                     @Validated @RequestBody CancelLessonRequest cancelLessonRequest) {
        log.info("강의 취소 처리 요청: {} {}", user.getName(), scheduleId);
        return BaseResponse.of(LESSON_SCHEDULE_CANCEL_OK, lessonService.cancelLessonSchedule(user, scheduleId, cancelLessonRequest));
    }


}
