package inha.dayoook_e.song.api.service;

import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchCountryResponse;
import inha.dayoook_e.mapping.api.mapper.MappingMapper;
import inha.dayoook_e.mapping.domain.Country;
import inha.dayoook_e.mapping.domain.repository.CountryJpaRepository;
import inha.dayoook_e.song.api.controller.dto.request.CreateSongRequest;
import inha.dayoook_e.song.api.controller.dto.request.SearchCond;
import inha.dayoook_e.song.api.controller.dto.response.LikedTuteeSongProgressResponse;
import inha.dayoook_e.song.api.controller.dto.response.SongResponse;
import inha.dayoook_e.song.api.controller.dto.response.SongSearchPageResponse;
import inha.dayoook_e.song.api.controller.dto.response.SongSearchResponse;
import inha.dayoook_e.song.api.mapper.SongMapper;
import inha.dayoook_e.song.domain.Song;
import inha.dayoook_e.song.domain.TuteeSongProgress;
import inha.dayoook_e.song.domain.repository.SongJpaRepository;
import inha.dayoook_e.song.domain.repository.SongQueryRepository;
import inha.dayoook_e.song.domain.repository.TuteeSongProgressJpaRepository;
import inha.dayoook_e.tutee.domain.TuteeInfo;
import inha.dayoook_e.tutee.domain.repository.TuteeInfoJpaRepository;
import inha.dayoook_e.user.domain.Point;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.repository.PointJpaRepository;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import inha.dayoook_e.utils.s3.S3Provider;
import inha.dayoook_e.utils.s3.dto.request.S3UploadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.common.Constant.*;
import static inha.dayoook_e.common.code.status.ErrorStatus.*;

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
    private final PointJpaRepository pointJpaRepository;
    private final TuteeInfoJpaRepository tuteeInfoJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final SongMapper songMapper;
    private final MappingMapper mappingMapper;
    private final SongQueryRepository songQueryRepository;
    private final S3Provider s3Provider;

    /**
     * 동요 목록 조회
     *
     * @param user 로그인한 사용자
     * @param searchCond 검색 조건
     * @param page 페이지 번호
     * @return 동요 목록 조회 결과
     */
    @Override
    @Transactional(readOnly = true)
    public Slice<SongSearchPageResponse> getSongs(User user, SearchCond searchCond, Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, CREATE_AT));
        return songQueryRepository.searchSongs(user, searchCond, pageable);
    }

    /**
     * 동요 상세 조회
     *
     * @param user 로그인한 사용자
     * @param songId 노래 ID
     * @return 동요 조회 결과
     */
    @Override
    @Transactional(readOnly = true)
    public SongSearchResponse getSong(User user, Integer songId) {
        // 1. 국가 ID와 노래 ID로 노래 조회
        Song song = songJpaRepository.findByIdAndState(songId, ACTIVE)
                .orElseThrow(() -> new BaseException(SONG_NOT_FOUND));

        // 2. 해당 사용자의 노래 진행상황 조회
        TuteeSongProgress progress = tuteeSongProgressJpaRepository
                .findByTutee_IdAndSong_Id(user.getId(), songId)
                .orElse(null);

        // 3. progress 정보가 없는 경우 기본값 false 사용
        boolean liked = progress != null && progress.getLiked();
        boolean completed = progress != null && progress.getIsCompleted();

        // 4. 국가 정보 변환
        SearchCountryResponse countryResponse = mappingMapper.toSearchCountryResponse(
                song.getCountry().getId(),
                song.getCountry().getName(),
                song.getCountry().getFlagUrl()
        );
        return songMapper.songToSongSearchResponse(song, countryResponse, liked, completed);
    }

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

    /**
     * 좋아요 토글
     *
     * @param user 로그인한 사용자
     * @param songId 노래 ID
     * @return 좋아요 토글 결과
     */
    @Override
    public LikedTuteeSongProgressResponse toggleLike(User user, Integer songId) {
        // 1. 노래 조회
        Song song = songJpaRepository.findById(songId)
                .orElseThrow(() -> new BaseException(SONG_NOT_FOUND));

        // 2. 사용자의 노래 진행상황 조회 또는 생성
        TuteeSongProgress progress = tuteeSongProgressJpaRepository
                .findByTutee_IdAndSong_Id(user.getId(), songId)
                .orElse(songMapper.toTuteeSongProgress(user, song));

        // 3. 좋아요 토글
        progress.toggleLike();

        tuteeSongProgressJpaRepository.save(progress);
        return songMapper.tuteeSongProgressToLikedTuteeSongProgressResponse(progress);
    }

    /**
     * 노래 완료 처리
     *
     * @param user 로그인한 사용자
     * @param songId 노래 ID
     * @return 노래 완료 처리 결과
     */
    @Override
    public SongResponse completeSong(User user, Integer songId) {
        // 1. 노래 조회
        Song song = songJpaRepository.findById(songId)
                .orElseThrow(() -> new BaseException(SONG_NOT_FOUND));

        // 2. 사용자의 노래 진행상황 조회 또는 생성
        TuteeSongProgress progress = tuteeSongProgressJpaRepository
                .findByTutee_IdAndSong_Id(user.getId(), songId)
                .orElse(songMapper.toTuteeSongProgress(user, song));

        // 3. 이미 완료된 노래인 경우 예외 처리
        if(Boolean.TRUE.equals(progress.getIsCompleted())) {
            throw new BaseException(SONG_ALREADY_COMPLETE);
        }

        // 4. 노래 완료 처리
        progress.completeSong();
        TuteeInfo tuteeInfo = tuteeInfoJpaRepository.findByuserId(user.getId())
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        tuteeInfo.addPoint(20);
        User findUser = userJpaRepository.findByIdAndState(user.getId(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        Point point = mappingMapper.createPoint(findUser, 20, songId + SONG_COMPLETE, LocalDateTime.now());
        pointJpaRepository.save(point);
        tuteeSongProgressJpaRepository.save(progress);
        return songMapper.songToSongResponse(song);
    }
}
