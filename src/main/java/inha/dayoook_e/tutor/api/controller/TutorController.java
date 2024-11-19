package inha.dayoook_e.tutor.api.controller;

import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.tutor.api.controller.dto.request.SearchCond;
import inha.dayoook_e.tutor.api.controller.dto.response.TutorSearchPageResponse;
import inha.dayoook_e.tutor.api.service.TutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static inha.dayoook_e.common.code.status.ErrorStatus.INVALID_PAGE;
import static inha.dayoook_e.common.code.status.SuccessStatus.TUTOR_SEARCH_PAGE_OK;


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

}
