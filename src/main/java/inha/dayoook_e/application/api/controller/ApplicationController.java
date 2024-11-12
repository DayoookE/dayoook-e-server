package inha.dayoook_e.application.api.controller;

import inha.dayoook_e.application.api.controller.dto.request.ApplyRequest;
import inha.dayoook_e.application.api.controller.dto.response.ApplicationResponse;
import inha.dayoook_e.application.api.service.ApplicationService;
import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.user.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

import static inha.dayoook_e.common.code.status.SuccessStatus.APPLICATION_CREATE_OK;
import static inha.dayoook_e.common.code.status.SuccessStatus.SONG_CREATE_OK;


/**
 * ApplicantionController 는 강의 신청 관련 엔드포인트를 관리
 */
@Slf4j
@Tag(name = "application controller", description = "강의 신청 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/application")
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<ApplicationResponse> apply(@AuthenticationPrincipal User user,
                                                   @ModelAttribute ApplyRequest applyRequest) {
        return BaseResponse.of(APPLICATION_CREATE_OK, applicationService.apply(user, applyRequest));
    }
}
