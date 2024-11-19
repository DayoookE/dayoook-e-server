package inha.dayoook_e.tutor.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchAgeGroupResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;
import inha.dayoook_e.tutor.api.controller.dto.request.SearchCond;
import inha.dayoook_e.tutor.api.controller.dto.response.TutorSearchPageResponse;
import inha.dayoook_e.tutor.api.mapper.TutorMapper;
import inha.dayoook_e.tutor.domain.TutorAgeGroup;
import inha.dayoook_e.user.api.mapper.UserMapper;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.UserLanguage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.user.domain.QUser.user;
import static inha.dayoook_e.tutor.domain.QTutorAgeGroup.tutorAgeGroup;
import static inha.dayoook_e.user.domain.QUserLanguage.userLanguage;

/**
 * TutorQueryRepository는 튜터 조회를 위한 쿼리 메서드를 제공.
 */
@Repository
@RequiredArgsConstructor
public class TutorQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final TutorMapper tutorMapper;
    private final UserMapper userMapper;

    /**
     * 쿼리를 통한 튜터 검색
     *
     * @param searchCond 검색 조건
     * @param pageable 페이징 정보
     * @return 튜터 검색 결과
     */
    public Slice<TutorSearchPageResponse> searchTutors(SearchCond searchCond, Pageable pageable) {
        // 1. 기본 검색 조건으로 데이터 조회
        List<User> tutors = queryFactory
                .selectFrom(user)
                .leftJoin(tutorAgeGroup)
                .on(user.id.eq(tutorAgeGroup.tutorInfo.userId))
                .leftJoin(userLanguage)
                .on(user.id.eq(userLanguage.user.id))
                .where(
                        languageIdEq(searchCond.languageId()),
                        ageGroupIdEq(searchCond.ageGroupId()),
                        user.state.eq(ACTIVE)
                )
                .orderBy(user.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // Slice를 위해 limit + 1
                .fetch();

        // 2. Slice 생성을 위한 hasNext 체크
        boolean hasNext = false;
        if (tutors.size() > pageable.getPageSize()) {
            hasNext = true;
            tutors.remove(pageable.getPageSize());
        }

        // 3. 튜터 ID 목록 추출
        List<Integer> tutorIds = tutors.stream()
                .map(User::getId)
                .toList();

        // 4-1. 언어 정보 리스트 조회
        List<UserLanguage> userLanguageList = queryFactory
                .selectFrom(userLanguage)
                .where(
                        userLanguage.user.id.in(tutorIds)
                )
                .fetch();

        // 4-2. 언어 정보 리스트를 SearchLanguageResponse로 변환
        List<SearchLanguagesResponse> searchLanguagesResponses = userLanguageList.stream().map(
                language -> userMapper.userLanguageToSearchLanguageResponse(language)
        ).toList();

        // 5-1. 연령대 정보 리스트 조회
        List<TutorAgeGroup> tutorAgeGroupList = queryFactory
                .selectFrom(tutorAgeGroup)
                .where(
                        tutorAgeGroup.tutorInfo.userId.in(tutorIds)
                )
                .fetch();

        // 5-2. 연령대 정보 리스트를 SearchAgeGroupResponseList로 변환
        List<SearchAgeGroupResponse> searchAgeGroupResponses = tutorAgeGroupList.stream().map(
                ageGroup -> tutorMapper.toSearchAgeGroupResponse(ageGroup)
        ).toList();

        // 6. 응답 DTO 변환
        List<TutorSearchPageResponse> content = tutors.stream()
                .map(tutor -> {
                    return tutorMapper.toTutorSearchPageResponse(tutor, searchLanguagesResponses, searchAgeGroupResponses);
                })
                .toList();

        return new SliceImpl<>(content, pageable, hasNext);
    }

    /**
     * 언어 ID 검색 조건
     */
    private BooleanExpression languageIdEq(Integer languageId) {
        return languageId != null ? userLanguage.language.id.eq(languageId) : null;
    }

    /**
     * 연령대 ID 검색 조건
     */
    private BooleanExpression ageGroupIdEq(Integer ageGroupId) {
        return ageGroupId != null ? tutorAgeGroup.ageGroup.id.eq(ageGroupId) : null;
    }
}
