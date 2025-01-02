package inha.dayoook_e.song.api.controller;

import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.song.api.controller.dto.request.CreateSongRequest;
import inha.dayoook_e.song.api.controller.dto.request.SearchCond;
import inha.dayoook_e.song.api.controller.dto.response.LikedTuteeSongProgressResponse;
import inha.dayoook_e.song.api.controller.dto.response.SongResponse;
import inha.dayoook_e.song.api.controller.dto.response.SongSearchPageResponse;
import inha.dayoook_e.song.api.controller.dto.response.SongSearchResponse;
import inha.dayoook_e.song.api.service.SongService;
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

import static inha.dayoook_e.common.code.status.ErrorStatus.INVALID_PAGE;
import static inha.dayoook_e.common.code.status.SuccessStatus.*;


/**
 * SongController은 동요 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "song controller", description = "동요 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/songs")
public class SongController {

    private final SongService songService;

    /**
     * 동요 조회 API
     *
     * <p>동요를 조회합니다.</p>
     *
     * @param searchCond 검색 조건
     * @param page 페이지 번호
     * @return 동요 조회 결과를 포함하는 BaseResponse<Slice<SongSearchPageResponse>>
     */
    @GetMapping
    @Operation(summary = "동요 조건 조회 API", description = "동요를 조건 조회합니다.")
    public BaseResponse<Slice<SongSearchPageResponse>> getSongs(
            @AuthenticationPrincipal User user,
            @Validated @ModelAttribute SearchCond searchCond,
            @RequestParam("page") Integer page
    ) {
        if (page < 1) {
            throw new BaseException(INVALID_PAGE);
        }
        return BaseResponse.of(SONG_SEARCH_PAGE_OK, songService.getSongs(user, searchCond, page - 1));
    }

    /**
     * 동요 상세 조회 API
     *
     * <p>동요를 상세 조회합니다.</p>
     *
     * @param user 로그인한 사용자
     * @param songId 동요 ID
     * @return 동요 상세 조회 결과를 포함하는 BaseResponse<SongSearchResponse>
     */
    @GetMapping("/{songId}")
    @Operation(summary = "동요 상세 조회 API", description = "동요를 상세 조회합니다.")
    public BaseResponse<SongSearchResponse> getSong(
            @AuthenticationPrincipal User user,
            @PathVariable("songId") Integer songId) {
        return BaseResponse.of(SONG_SEARCH_OK, songService.getSong(user, songId));
    }

    /**
     * 동요 생성 API
     *
     * <p>동요를 생성합니다.</p>
     *
     * @param createSongRequest 동요 생성 요청
     * @param thumbnail 썸네일 이미지
     * @param media 미디어 파일
     * @return 동요 생성 결과를 포함하는 BaseResponse<SongResponse>
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "동요 생성(admin 전용) API", description = "동요를 생성합니다.")
    public BaseResponse<SongResponse> createSong(@AuthenticationPrincipal User user,
                                                 @Validated @RequestPart("song") CreateSongRequest createSongRequest,
                                                 @RequestPart("thumbnail") MultipartFile thumbnail,
                                                 @RequestPart("media") MultipartFile media) {
        return BaseResponse.of(SONG_CREATE_OK, songService.createSong(user, createSongRequest, thumbnail, media));
    }

    /**
     * 좋아요 토글 API
     *
     * <p>좋아요를 토글합니다.</p>
     *
     * @param user 로그인한 사용자
     * @param songId 동요 ID
     * @return 좋아요 토글 결과를 포함하는 BaseResponse<LikedTuteeSongProgressResponse>
     */
    @PostMapping("/{songId}/toggle-like")
    @Operation(summary = "좋아요 토글 API", description = "좋아요를 토글합니다.")
    public BaseResponse<LikedTuteeSongProgressResponse> toggleLike(@AuthenticationPrincipal User user,
                                                                   @PathVariable("songId") Integer songId) {
        return BaseResponse.of(SONG_TOGGLE_LIKE_OK, songService.toggleLike(user, songId));
    }

    /**
     * 동요 완료 API
     *
     * <p>동요 듣는 것을 완료합니다.</p>
     *
     * @param user 로그인한 사용자
     * @param songId 동요 ID
     * @return 동요 완료 결과를 포함하는 BaseResponse<SongResponse>
     */
    @PostMapping("/{songId}/complete")
    @Operation(summary = "동요 완료 API", description = "동요 듣는 것을 완료합니다.")
    public BaseResponse<SongResponse> completeSong(@AuthenticationPrincipal User user,
                                                   @PathVariable("songId") Integer songId) {
        return BaseResponse.of(SONG_COMPLETE_OK, songService.completeSong(user, songId));
    }


}
