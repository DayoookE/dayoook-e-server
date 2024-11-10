package inha.dayoook_e.song.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inha.dayoook_e.song.api.controller.dto.request.SearchCond;
import inha.dayoook_e.song.api.controller.dto.response.SongSearchPageResponse;
import inha.dayoook_e.song.api.mapper.SongMapper;
import inha.dayoook_e.song.domain.Song;
import inha.dayoook_e.song.domain.TuteeSongProgress;
import inha.dayoook_e.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.song.domain.QSong.song;
import static inha.dayoook_e.song.domain.QTuteeSongProgress.tuteeSongProgress;

@Repository
@RequiredArgsConstructor
public class SongQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final SongMapper songMapper;

    public Slice<SongSearchPageResponse> searchSongs(User user, SearchCond searchCond, Pageable pageable) {
        // 1. 기본 검색 조건으로 데이터 조회
        List<Song> songs = queryFactory
                .selectFrom(song)
                .leftJoin(tuteeSongProgress)
                .on(song.id.eq(tuteeSongProgress.song.id)
                        .and(tuteeSongProgress.tutee.id.eq(user.getId())))
                .where(
                        countryIdEq(searchCond.countryId()),
                        likedEq(searchCond.liked(), user.getId()),
                        titleLike(searchCond.title()),
                        song.state.eq(ACTIVE)
                )
                .orderBy(song.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // Slice를 위해 limit + 1
                .fetch();

        // 2. Slice 생성을 위한 hasNext 체크
        boolean hasNext = false;
        if (songs.size() > pageable.getPageSize()) {
            hasNext = true;
            songs.remove(pageable.getPageSize());
        }

        // 3. 노래 ID 목록 추출
        List<Integer> songIds = songs.stream()
                .map(Song::getId)
                .toList();

        // 4. 진행상황 맵 조회
        Map<Integer, TuteeSongProgress> progressMap = queryFactory
                .selectFrom(tuteeSongProgress)
                .where(
                        tuteeSongProgress.tutee.id.eq(user.getId()),
                        tuteeSongProgress.song.id.in(songIds)
                )
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        progress -> progress.getSong().getId(),
                        progress -> progress
                ));

        // 5. 응답 DTO 변환
        List<SongSearchPageResponse> content = songs.stream()
                .map(song -> {
                    TuteeSongProgress progress = progressMap.get(song.getId());
                    boolean liked = progress != null && progress.getLiked();
                    boolean completed = progress != null && progress.getIsCompleted();
                    return songMapper.songToSongSearchPageResponse(song, liked, completed);
                })
                .toList();

        return new SliceImpl<>(content, pageable, hasNext);
    }

    /**
     * 국가 ID 검색 조건
     */
    private BooleanExpression countryIdEq(Integer countryId) {
        return countryId != null ? song.country.id.eq(countryId) : null;
    }

    /**
     * 좋아요 검색 조건
     */
    private BooleanExpression likedEq(Boolean liked, Integer userId) {
        if (liked == null) return null;
        return liked ?
                tuteeSongProgress.tutee.id.eq(userId)
                        .and(tuteeSongProgress.liked.isTrue()) :
                tuteeSongProgress.isNull()
                        .or(tuteeSongProgress.tutee.id.eq(userId)
                                .and(tuteeSongProgress.liked.isFalse()));
    }

    /**
     * 제목 검색 조건
     */
    private BooleanExpression titleLike(String title) {
        return StringUtils.hasText(title) ? song.title.contains(title) : null;
    }
}
