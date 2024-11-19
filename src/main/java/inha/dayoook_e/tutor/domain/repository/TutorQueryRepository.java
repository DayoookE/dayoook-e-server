package inha.dayoook_e.tutor.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchAgeGroupResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;
import inha.dayoook_e.mapping.api.mapper.MappingMapper;
import inha.dayoook_e.tutor.api.controller.dto.request.SearchCond;
import inha.dayoook_e.tutor.api.controller.dto.response.TutorSearchPageResponse;
import inha.dayoook_e.tutor.api.mapper.TutorMapper;
import inha.dayoook_e.tutor.domain.TutorAgeGroup;
import inha.dayoook_e.tutor.domain.TutorInfo;
import inha.dayoook_e.user.api.mapper.UserMapper;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.UserLanguage;
import inha.dayoook_e.user.domain.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.user.domain.QUser.user;
import static inha.dayoook_e.tutor.domain.QTutorInfo.tutorInfo;
import static inha.dayoook_e.tutor.domain.QTutorAgeGroup.tutorAgeGroup;
import static inha.dayoook_e.user.domain.QUserLanguage.userLanguage;

@Repository
@RequiredArgsConstructor
public class TutorQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final TutorMapper tutorMapper;
    private final UserMapper userMapper;
    private final MappingMapper mappingMapper;

    public Slice<TutorSearchPageResponse> searchTutors(SearchCond searchCond, Pageable pageable) {
        // 1. 언어 조건이 있는 경우, 해당 언어들을 모두 가지고 있는 튜터 ID 조회
        List<Integer> tutorIdsWithAllLanguages = null;
        if (searchCond.languageId() != null && !searchCond.languageId().isEmpty()) {
            tutorIdsWithAllLanguages = queryFactory
                    .select(userLanguage.user.id)
                    .from(userLanguage)
                    .where(userLanguage.language.id.in(searchCond.languageId()))
                    .groupBy(userLanguage.user.id)
                    .having(userLanguage.language.id.count().goe((long) searchCond.languageId().size()))
                    .fetch();
        }

        // 2. 기본 검색 조건으로 튜터와 튜터 정보 조회
        List<User> tutors = queryFactory
                .selectFrom(user)
                .join(tutorInfo).on(user.id.eq(tutorInfo.userId))
                .where(
                        user.state.eq(ACTIVE),
                        user.role.eq(Role.TUTOR),
                        nameContains(searchCond.name()),
                        tutorIdsWithAllLanguages != null ?
                                user.id.in(tutorIdsWithAllLanguages) : null,
                        searchCond.ageGroupId() != null && !searchCond.ageGroupId().isEmpty() ?
                                user.id.in(
                                        JPAExpressions
                                                .select(tutorAgeGroup.tutorInfo.userId)
                                                .from(tutorAgeGroup)
                                                .where(tutorAgeGroup.ageGroup.id.in(searchCond.ageGroupId()))
                                ) : null
                )
                .orderBy(user.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        // 3. Slice 생성을 위한 hasNext 체크
        boolean hasNext = false;
        if (tutors.size() > pageable.getPageSize()) {
            hasNext = true;
            tutors.remove(pageable.getPageSize());
        }

        // 4. 튜터 ID 목록 추출
        List<Integer> tutorIds = tutors.stream()
                .map(User::getId)
                .toList();

        // 5. 튜터 정보(rating 포함) 조회
        Map<Integer, TutorInfo> tutorInfoMap = queryFactory
                .selectFrom(tutorInfo)
                .where(tutorInfo.userId.in(tutorIds))
                .fetch()
                .stream()
                .collect(Collectors.toMap(TutorInfo::getUserId, ti -> ti));

        // 6. 언어 정보 리스트 조회
        List<UserLanguage> userLanguageList = queryFactory
                .selectFrom(userLanguage)
                .where(userLanguage.user.id.in(tutorIds))
                .fetch();

        // 7. 언어 정보 리스트를 SearchLanguageResponse로 변환
        Map<Integer, List<SearchLanguagesResponse>> userLanguagesMap = userLanguageList.stream()
                .collect(Collectors.groupingBy(
                        ul -> ul.getUser().getId(),
                        Collectors.mapping(
                                mappingMapper::userLanguageToSearchLanguageResponse,
                                Collectors.toList()
                        )
                ));

        // 8. 연령대 정보 리스트 조회
        List<TutorAgeGroup> tutorAgeGroupList = queryFactory
                .selectFrom(tutorAgeGroup)
                .where(tutorAgeGroup.tutorInfo.userId.in(tutorIds))
                .fetch();

        // 9. 연령대 정보 리스트를 SearchAgeGroupResponse로 변환
        Map<Integer, List<SearchAgeGroupResponse>> ageGroupMap = tutorAgeGroupList.stream()
                .collect(Collectors.groupingBy(
                        tag -> tag.getTutorInfo().getUserId(),
                        Collectors.mapping(
                                mappingMapper::toSearchAgeGroupResponse,
                                Collectors.toList()
                        )
                ));

        // 10. 응답 DTO 변환
        List<TutorSearchPageResponse> content = tutors.stream()
                .map(tutor -> {
                    TutorInfo info = tutorInfoMap.get(tutor.getId());
                    return new TutorSearchPageResponse(
                            tutor.getId(),
                            tutor.getName(),
                            info != null ? info.getRating() : 0.0,
                            ageGroupMap.getOrDefault(tutor.getId(), Collections.emptyList()),
                            userLanguagesMap.getOrDefault(tutor.getId(), Collections.emptyList())
                    );
                })
                .toList();

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression nameContains(String name) {
        return StringUtils.hasText(name) ? user.name.contains(name) : null;
    }
}