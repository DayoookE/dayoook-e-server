package inha.dayoook_e.tutee.api.controller;

import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.tutee.api.controller.dto.response.SearchTuteeApplicationResponse;
import inha.dayoook_e.tutee.api.service.TuteeService;
import inha.dayoook_e.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static inha.dayoook_e.common.code.status.ErrorStatus.INVALID_PAGE;
import static inha.dayoook_e.common.code.status.SuccessStatus.TUTEE_APPLICATION_SEARCH_OK;


/**
 * TuteeController은 튜티 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "tutee controller", description = "튜티 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tutees")
public class TuteeController {

    private final TuteeService tuteeService;


    /**
     * 튜티 신청 내역 조회 API
     *
     * <p>튜티가 신청한 수업 목록을 조회합니다.</p>
     *
     * @param user 사용자 정보
     * @param page 페이지 번호
     * @param status 상태
     * @return 튜티 신청 내역 조회 결과를 포함하는 BaseResponse<Slice<SearchTuteeApplicationResponse>>
     */
    @GetMapping("/applications")
    @Operation(summary = "튜티 신청 내역 조회 API", description = "튜티가 신청한 수업 목록을 조회합니다.")
    public BaseResponse<Slice<SearchTuteeApplicationResponse>> getTuteeApplications(@AuthenticationPrincipal User user,
                                                                                    @RequestParam("page") Integer page,
                                                                                    @RequestParam(value = "status", required = false) Status status) {
        if (page < 1) {
            throw new BaseException(INVALID_PAGE);
        }
        return BaseResponse.of(TUTEE_APPLICATION_SEARCH_OK, tuteeService.getTuteeApplications(user, page - 1, status));
    }
}
