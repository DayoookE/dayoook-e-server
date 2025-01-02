package inha.dayoook_e.tutor.api.controller;

import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.tutor.api.controller.dto.request.SearchCond;
import inha.dayoook_e.tutor.api.controller.dto.request.TutorScheduleRequest;
import inha.dayoook_e.tutor.api.controller.dto.response.*;
import inha.dayoook_e.tutor.api.service.TutorService;
import inha.dayoook_e.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static inha.dayoook_e.common.code.status.ErrorStatus.INVALID_PAGE;
import static inha.dayoook_e.common.code.status.SuccessStatus.*;


/**
 * TutorController는 튜터 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "tutor controller", description = "튜터 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tutors")
public class TutorController {

    private final TutorService tutorService;

    /**
     * 튜터 조회 API
     *
     * <p>튜터를 조회합니다.</p>
     *
     * @param searchCond 검색 조건
     * @param page 페이지 번호
     * @return 튜터 조회 결과를 포함하는 BaseResponse<Slice<TutorSearchPageResponse>>
     */
    @GetMapping
    @Operation(summary = "튜터 조건 조회 API", description = "튜터를 조건 조회합니다.")
    public BaseResponse<Slice<TutorSearchPageResponse>> getTutors(
            @Validated @ModelAttribute SearchCond searchCond,
            @RequestParam("page") Integer page
    ) {
        if (page < 1) {
            throw new BaseException(INVALID_PAGE);
        }
        return BaseResponse.of(TUTOR_SEARCH_PAGE_OK, tutorService.getTutors(searchCond, page - 1));
    }

    @GetMapping("/{tutorId}")
    @Operation(summary = "튜터 상세 조회 API", description = "튜터를 상세 조회합니다.")
    public BaseResponse<TutorSearchResponse> getTutor(
            @PathVariable("tutorId") Integer tutorId) {
        return BaseResponse.of(TUTOR_SEARCH_OK, tutorService.getTutor(tutorId));
    }

    /**
     * 튜터 일정 조회 API
     *
     * <p>튜터 일정을 조회합니다.</p>
     *
     * @param user 사용자 정보
     * @param tutorId 튜터 ID
     * @return 튜터 일정 조회 결과를 포함하는 BaseResponse<SearchTutorScheduleResponse>
     */
    @GetMapping("/schedule/{tutorId}")
    @Operation(summary = "튜터 일정 조회 API", description = "튜터 일정을 조회합니다.")
    public BaseResponse<SearchTutorScheduleResponse> getTutorSchedule(@AuthenticationPrincipal User user,
                                                                      @PathVariable("tutorId") Integer tutorId) {
        return BaseResponse.of(TUTOR_SCHEDULE_SEARCH_OK, tutorService.getTutorSchedule(user, tutorId));
    }

    @GetMapping("/application/{tutorId}")
    @Operation(summary = "튜터 신청 조회 API", description = "튜터 신청을 조회합니다.")
    public BaseResponse<Page<SearchTutorApplicationResponse>> getTutorApplication(@AuthenticationPrincipal User user,
                                                                                  @PathVariable("tutorId") Integer tutorId,
                                                                                  @RequestParam("page") Integer page,
                                                                                  @RequestParam(value = "status", required = false) Status status) {
        if (page < 1) {
            throw new BaseException(INVALID_PAGE);
        }
        return BaseResponse.of(TUTOR_APPLICATION_SEARCH_OK, tutorService.getTutorApplication(user, tutorId, page - 1, status));
    }

    /**
     * 튜터 일정 생성 API
     *
     * <p>튜터 일정을 생성합니다.</p>
     *
     * @param user 사용자 정보
     * @param tutorScheduleRequest 튜터 일정 생성 요청
     * @return 튜터 일정 생성 결과를 포함하는 BaseResponse<List<TutorScheduleResponse>>
     */
    @PostMapping("/schedule")
    @Operation(summary = "튜터 일정 생성 API", description = "튜터 일정을 생성합니다.")
    public BaseResponse<TutorResponse> createSchedule(@AuthenticationPrincipal User user,
                                                                    @Validated @RequestBody TutorScheduleRequest tutorScheduleRequest) {
        return BaseResponse.of(TUTOR_SCHEDULE_CREATE_OK ,tutorService.createSchedule(user, tutorScheduleRequest));
    }
}
