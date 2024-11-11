package inha.dayoook_e.storybook.api.controller;

import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.storybook.api.controller.dto.request.CreateStorybookRequest;
import inha.dayoook_e.storybook.api.controller.dto.request.SearchCond;
import inha.dayoook_e.storybook.api.controller.dto.response.LikedTuteeStorybookProgressResponse;
import inha.dayoook_e.storybook.api.controller.dto.response.StorybookResponse;
import inha.dayoook_e.storybook.api.controller.dto.response.StorybookSearchPageResponse;
import inha.dayoook_e.storybook.api.service.StorybookService;
import inha.dayoook_e.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static inha.dayoook_e.common.code.status.ErrorStatus.INVALID_PAGE;
import static inha.dayoook_e.common.code.status.SuccessStatus.*;


/**
 * StorybookController은 동화 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "storybook controller", description = "동화 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/storybooks")
public class StorybookController {

    private final StorybookService storybookService;

    /**
     * 동화 조회 API
     *
     * <p>동화를 조회합니다.</p>
     *
     * @param searchCond 검색 조건
     * @param page 페이지 번호
     * @return 동화 조회 결과를 포함하는 BaseResponse<Slice<StorybookSearchPageResponse>>
     */
    @GetMapping
    @Operation(summary = "동화 조회 API", description = "동화를 조회합니다.")
    public BaseResponse<Slice<StorybookSearchPageResponse>> getStorybooks(
            @AuthenticationPrincipal User user,
            @Validated @ModelAttribute SearchCond searchCond,
            @RequestParam("page") Integer page
    ) {
        if (page < 1) {
            throw new BaseException(INVALID_PAGE);
        }
        return BaseResponse.of(STORYBOOK_SEARCH_PAGE_OK, storybookService.getStorybooks(user, searchCond, page - 1));
    }

    /**
     * 동화 생성 API
     *
     * <p>동화를 생성합니다.</p>
     *
     * @param user 로그인한 사용자
     * @param createStorybookRequest 동화 생성 요청
     * @param thumbnail 썸네일 이미지
     * @param pageImages 페이지 이미지 리스트
     * @return 동화 생성 결과를 포함하는 BaseResponse<StorybookResponse>
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "동화 생성(admin 전용) API", description = "동화를 생성합니다.")
    public BaseResponse<StorybookResponse> createStorybook(@AuthenticationPrincipal User user,
                                                           @Validated @RequestPart("storybook") CreateStorybookRequest createStorybookRequest,
                                                           @RequestPart("thumbnail") MultipartFile thumbnail,
                                                           @RequestPart("pages") List<MultipartFile> pageImages) {
        return BaseResponse.of(STORYBOOK_CREATE_OK, storybookService.createStorybook(user, createStorybookRequest, thumbnail, pageImages));
    }


    /**
     * 동화 좋아요 토글 API
     *
     * <p>좋아요를 토글합니다.</p>
     *
     * @param user 로그인한 사용자
     * @param storybookId 동화 id
     * @return 좋아요 토글 결과를 포함하는 BaseResponse<LikedTuteeStorybookProgressResponse>
     */
    @PostMapping("/{storybookId}/toggle-like")
    @Operation(summary = "좋아요 토글 API", description = "좋아요를 토글합니다.")
    public BaseResponse<LikedTuteeStorybookProgressResponse> toggleLike(@AuthenticationPrincipal User user,
                                                                        @PathVariable("storybookId") Integer storybookId) {
        return BaseResponse.of(STORYBOOK_TOGGLE_LIKE_OK, storybookService.toggleLike(user, storybookId));
    }

    /**
     * 동화 완료 API
     *
     * <p>동화 듣는 것을 완료합니다.</p>
     *
     * @param user 로그인한 사용자
     * @param storybookId 동화 id
     * @return 동화 완료 결과를 포함하는 BaseResponse<StorybookResponse>
     */
    @PostMapping("/{storybookId}/complete")
    @Operation(summary = "동화 완료 API", description = "동화 듣는 것을 완료합니다.")
    public BaseResponse<StorybookResponse> completeStorybook(@AuthenticationPrincipal User user,
                                                             @PathVariable("storybookId") Integer storybookId) {
        return BaseResponse.of(STORYBOOK_COMPLETE_OK, storybookService.completeStorybook(user, storybookId));
    }
}
