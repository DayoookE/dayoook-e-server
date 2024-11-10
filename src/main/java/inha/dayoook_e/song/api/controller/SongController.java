package inha.dayoook_e.song.api.controller;

import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.song.api.controller.dto.request.CreateSongRequest;
import inha.dayoook_e.song.api.controller.dto.response.SongResponse;
import inha.dayoook_e.song.api.service.SongService;
import inha.dayoook_e.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static inha.dayoook_e.common.code.status.SuccessStatus.SONG_CREATE_OK;


/**
 * SongController은 동요 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "song controller", description = "동요 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/songs")
public class SongController {

    private final SongService songService;

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
    @Operation(summary = "동요 생성 API", description = "동요를 생성합니다.")
    public BaseResponse<SongResponse> createSong(@AuthenticationPrincipal User user,
                                                 @Validated @RequestPart("song") CreateSongRequest createSongRequest,
                                                 @RequestPart("thumbnail") MultipartFile thumbnail,
                                                 @RequestPart("media") MultipartFile media) {
        return BaseResponse.of(SONG_CREATE_OK, songService.createSong(user, createSongRequest, thumbnail, media));
    }

}
