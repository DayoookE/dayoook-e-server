package inha.dayoook_e.lesson.api.controller;

import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonScheduleRequest;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonScheduleResponse;
import inha.dayoook_e.lesson.api.service.LessonService;
import inha.dayoook_e.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static inha.dayoook_e.common.code.status.SuccessStatus.LESSON_SCHEDULE_CREATE_OK;


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
            @Validated @RequestBody CreateLessonScheduleRequest createLessonScheduleRequest) {
        log.info("강의 일정 생성 요청: {} {}", user.getName(), createLessonScheduleRequest);
        return BaseResponse.of(LESSON_SCHEDULE_CREATE_OK, lessonService.createLessonSchedule(user, createLessonScheduleRequest));
    }

}
