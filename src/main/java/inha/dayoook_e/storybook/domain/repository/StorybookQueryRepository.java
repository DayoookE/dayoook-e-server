package inha.dayoook_e.storybook.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inha.dayoook_e.storybook.api.controller.dto.request.SearchCond;
import inha.dayoook_e.storybook.api.controller.dto.response.StorybookSearchPageResponse;
import inha.dayoook_e.storybook.api.mapper.StorybookMapper;
import inha.dayoook_e.storybook.domain.Storybook;
import inha.dayoook_e.storybook.domain.TuteeStoryProgress;
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
import static inha.dayoook_e.storybook.domain.QStorybook.storybook;
import static inha.dayoook_e.storybook.domain.QTuteeStoryProgress.tuteeStoryProgress;

/**
 * StorybookQueryRepository는 동화 조회를 위한 쿼리 메서드를 제공.
 */
@Repository
@RequiredArgsConstructor
public class StorybookQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final StorybookMapper storybookMapper;

    public Slice<StorybookSearchPageResponse> searchStorybooks(User user, SearchCond searchCond, Pageable pageable) {
        // 1. 기본 검색 조건으로 데이터 조회
        List<Storybook> storybooks = queryFactory
                .selectFrom(storybook)
                .leftJoin(tuteeStoryProgress)
                .on(storybook.id.eq(tuteeStoryProgress.storybook.id)
                        .and(tuteeStoryProgress.tutee.id.eq(user.getId())))
                .where(
                        countryIdEq(searchCond.countryId()),
                        likedEq(searchCond.liked(), user.getId()),
                        titleLike(searchCond.title()),
                        storybook.state.eq(ACTIVE)
                )
                .orderBy(storybook.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // Slice를 위해 limit + 1
                .fetch();

        // 2. Slice 생성을 위한 hasNext 체크
        boolean hasNext = false;
        if (storybooks.size() > pageable.getPageSize()) {
            hasNext = true;
            storybooks.remove(pageable.getPageSize());
        }

        // 3. 동화 ID 목록 추출
        List<Integer> storybookIds = storybooks.stream()
                .map(Storybook::getId)
                .toList();

        // 4. 진행상황 맵 조회
        Map<Integer, TuteeStoryProgress> progressMap = queryFactory
                .selectFrom(tuteeStoryProgress)
                .where(
                        tuteeStoryProgress.tutee.id.eq(user.getId()),
                        tuteeStoryProgress.storybook.id.in(storybookIds)
                )
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        progress -> progress.getStorybook().getId(),
                        progress -> progress
                ));

        // 5. 응답 DTO 변환
        List<StorybookSearchPageResponse> content = storybooks.stream()
                .map(storybook -> {
                    TuteeStoryProgress progress = progressMap.get(storybook.getId());
                    boolean liked = progress != null && progress.getLiked();
                    int lastPageNumber = progress != null ? progress.getLastPageNumber() : 1;
                    boolean isCompleted = progress != null && progress.getIsCompleted();
                    return storybookMapper.storybookToStorybookSearchPageResponse(storybook, liked, lastPageNumber, isCompleted);
                })
                .toList();

        return new SliceImpl<>(content, pageable, hasNext);
    }

    /**
     * 국가 ID 검색 조건
     */
    private BooleanExpression countryIdEq(Integer countryId) {
        return countryId != null ? storybook.country.id.eq(countryId) : null;
    }

    /**
     * 좋아요 검색 조건
     */
    private BooleanExpression likedEq(Boolean liked, Integer userId) {
        if (liked == null) return null;
        return liked ?
                tuteeStoryProgress.tutee.id.eq(userId)
                        .and(tuteeStoryProgress.liked.isTrue()) :
                tuteeStoryProgress.isNull()
                        .or(tuteeStoryProgress.tutee.id.eq(userId)
                                .and(tuteeStoryProgress.liked.isFalse()));
    }

    /**
     * 제목 검색 조건
     */
    private BooleanExpression titleLike(String title) {
        return StringUtils.hasText(title) ? storybook.title.contains(title) : null;
    }
}