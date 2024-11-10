package inha.dayoook_e.song.api.service;

import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.mapping.api.dto.response.SearchCountryResponse;
import inha.dayoook_e.mapping.api.mapper.MappingMapper;
import inha.dayoook_e.mapping.domain.Country;
import inha.dayoook_e.mapping.domain.repository.CountryJpaRepository;
import inha.dayoook_e.song.api.controller.dto.request.CreateSongRequest;
import inha.dayoook_e.song.api.controller.dto.response.LikedTuteeSongProgressResponse;
import inha.dayoook_e.song.api.controller.dto.response.SongResponse;
import inha.dayoook_e.song.api.controller.dto.response.SongSearchPageResponse;
import inha.dayoook_e.song.api.controller.dto.response.SongSearchResponse;
import inha.dayoook_e.song.api.mapper.SongMapper;
import inha.dayoook_e.song.domain.Song;
import inha.dayoook_e.song.domain.TuteeSongProgress;
import inha.dayoook_e.song.domain.repository.SongJpaRepository;
import inha.dayoook_e.song.domain.repository.TuteeSongProgressJpaRepository;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.utils.s3.S3Provider;
import inha.dayoook_e.utils.s3.dto.request.S3UploadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.common.Constant.*;
import static inha.dayoook_e.common.code.status.ErrorStatus.COUNTRY_NOT_FOUND;
import static inha.dayoook_e.common.code.status.ErrorStatus.SONG_NOT_FOUND;

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
    private final MappingMapper mappingMapper;
    private final S3Provider s3Provider;

    /**
     * 동요 조회
     *
     * @param user 로그인한 사용자
     * @param countryId 국가 ID
     * @param page 페이지 번호
     * @return 동요 조회 결과
     */
    @Override
    @Transactional(readOnly = true)
    public Slice<SongSearchPageResponse> getSongs(User user, Integer countryId, Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, CREATE_AT));

        // 1. 해당 국가의 모든 노래를 슬라이싱하여 가져옴
        Slice<Song> songs = songJpaRepository.findAllByCountry_IdAndState(countryId, pageable, ACTIVE);

        // 2. TuteeSongProgress 조회를 위한 노래 ID 리스트
        List<Integer> songIds = songs.getContent().stream()
                .map(Song::getId)
                .toList();

        // 3. 유저의 진행상황 맵 생성 (노래 ID를 키로 사용)
        Map<Integer, TuteeSongProgress> progressMap = tuteeSongProgressJpaRepository
                .findAllByTutee_IdAndSong_IdIn(user.getId(), songIds)
                .stream()
                .collect(Collectors.toMap(
                        progress -> progress.getSong().getId(),
                        progress -> progress
                ));
        return songs.map(song -> {
            TuteeSongProgress progress = progressMap.get(song.getId());
            boolean liked = progress != null && progress.getLiked();
            boolean completed = progress != null && progress.getIsCompleted();
            return songMapper.songToSongSearchPageResponse(song, liked, completed);
        });
    }

    /**
     * 동요 조회
     *
     * @param user 로그인한 사용자
     * @param countryId 국가 ID
     * @param songId 노래 ID
     * @return 동요 조회 결과
     */
    @Override
    @Transactional(readOnly = true)
    public SongSearchResponse getSong(User user, Integer countryId, Integer songId) {
        // 1. 국가 ID와 노래 ID로 노래 조회
        Song song = songJpaRepository.findByIdAndCountry_IdAndState(songId, countryId, ACTIVE)
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
}
