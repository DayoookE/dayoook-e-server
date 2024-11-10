package inha.dayoook_e.song.api.service;

import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.mapping.domain.Country;
import inha.dayoook_e.mapping.domain.repository.CountryJpaRepository;
import inha.dayoook_e.song.api.controller.dto.request.CreateSongRequest;
import inha.dayoook_e.song.api.controller.dto.response.SongResponse;
import inha.dayoook_e.song.api.mapper.SongMapper;
import inha.dayoook_e.song.domain.Song;
import inha.dayoook_e.song.domain.repository.SongJpaRepository;
import inha.dayoook_e.song.domain.repository.TuteeSongProgressJpaRepository;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.utils.s3.S3Provider;
import inha.dayoook_e.utils.s3.dto.request.S3UploadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static inha.dayoook_e.common.Constant.*;
import static inha.dayoook_e.common.code.status.ErrorStatus.COUNTRY_NOT_FOUND;

/**
 * SongServiceImpl은 동요 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SongServiceImpl implements SongService {

    private final SongJpaRepository songJpaRepository;
    private final TuteeSongProgressJpaRepository tuteeSongProgressJpaRepository;
    private final CountryJpaRepository countryJpaRepository;
    private final SongMapper songMapper;
    private final S3Provider s3Provider;

    /**
     * 동요 생성
     *
     * @param user 로그인한 사용자
     * @param createSongRequest 동요 생성 요청
     * @param thumbnail 썸네일 이미지
     * @param media 미디어 파일
     * @return 동요 생성 결과
     */
    @Override
    public SongResponse createSong(User user, CreateSongRequest createSongRequest, MultipartFile thumbnail, MultipartFile media) {
        // 1. 국가 조회
        Country country = countryJpaRepository.findById(createSongRequest.countryId())
                .orElseThrow(() -> new BaseException(COUNTRY_NOT_FOUND));

        // 2. S3에 파일 업로드
        String thumbnailUrl = s3Provider.multipartFileUpload(thumbnail, new S3UploadRequest(user.getId(), SONG_THUMBNAIL_DIR));
        String mediaUrl = s3Provider.multipartFileUpload(media, new S3UploadRequest(user.getId(), SONG_MEDIA_DIR));


        // 3. 동요 생성
        Song song = songMapper.createSongRequestToSong(createSongRequest, country, thumbnailUrl, mediaUrl);
        Song savedSong = songJpaRepository.save(song);
        return songMapper.songToSongResponse(savedSong);
    }
}
