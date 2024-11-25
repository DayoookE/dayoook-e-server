package inha.dayoook_e.application.api.controller;

import inha.dayoook_e.application.api.controller.dto.request.ApplyRequest;
import inha.dayoook_e.application.api.controller.dto.response.ApplicationResponse;
import inha.dayoook_e.application.api.service.ApplicationService;
import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import static inha.dayoook_e.common.code.status.SuccessStatus.*;


/**
 * ApplicantionController 는 강의 신청 관련 엔드포인트를 관리
 */
@Slf4j
@Tag(name = "application controller", description = "강의 신청 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    /**
     * 강의 신청 API
     *
     * @param user 로그인한 사용자
     * @param applyRequest 강의 신청 정보
     * @return 강의 신청 생성 결과
     */
    @PostMapping
    @Operation(summary = "강의 신청 API", description = "튜터에게 강의 신청을 합니다.")
    public BaseResponse<ApplicationResponse> apply(@AuthenticationPrincipal User user,
                                                   @RequestBody ApplyRequest applyRequest) {
        return BaseResponse.of(APPLICATION_CREATE_OK, applicationService.apply(user, applyRequest));
    }

    /**
     * 강의 신청 승인 api
     * @param user 로그인 한 튜터
     * @param applicationGroupId 승인할 신청 그룹 ID
     * @return 강의 신청 승인 결과
     */
    @PostMapping("/{applicationGroupId}/approve")
    @Operation(summary = "강의 신청 승인 API", description = "튜터가 강의 신청을 승인합니다.")
    public BaseResponse<ApplicationResponse> approveApplication(@AuthenticationPrincipal User user,
                                              @PathVariable("applicationGroupId") Integer applicationGroupId) {
        return BaseResponse.of(APPLICATION_APPROVE_OK, applicationService.approveApplication(user, applicationGroupId));
    }

    /**
     * 강의 신청 거절 api
     * @param user 로그인 한 튜터
     * @param applicationGroupId 거절할 신청 그룹 ID
     * @return 강의 신청 거절 결과
     */
    @PostMapping("/{applicationGroupId}/deny")
    @Operation(summary = "강의 신청 거절 API", description = "튜터가 강의 신청을 거절합니다.")
    public BaseResponse<ApplicationResponse> rejectApplication(@AuthenticationPrincipal User user,
                                                   @PathVariable("applicationGroupId") Integer applicationGroupId) {
        return BaseResponse.of(APPLICATION_REJECT_OK, applicationService.rejectApplication(user, applicationGroupId));
    }

}
