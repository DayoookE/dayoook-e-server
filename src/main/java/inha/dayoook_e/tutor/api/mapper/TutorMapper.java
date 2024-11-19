package inha.dayoook_e.tutor.api.mapper;

import inha.dayoook_e.mapping.api.controller.dto.response.SearchAgeGroupResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;
import inha.dayoook_e.tutor.api.controller.dto.response.SearchExperienceResponse;
import inha.dayoook_e.tutor.api.controller.dto.response.TutorSearchPageResponse;
import inha.dayoook_e.tutor.api.controller.dto.response.TutorSearchResponse;
import inha.dayoook_e.tutor.domain.Experience;
import inha.dayoook_e.tutor.domain.TutorInfo;
import inha.dayoook_e.user.api.controller.dto.request.TutorSignupRequest;
import inha.dayoook_e.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * TutorMapper은 튜터와 관련된 데이터 변환 기능을 제공.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TutorMapper {

    /**
     * User와 TutorSignupRequest를 TutorInfo로 변환
     *
     * @param user 저장된 유저
     * @param tutorSignupRequest 튜터 회원가입 요청
     * @return TutorInfo
     */
    @Mapping(target = "rating", constant = "0.0")
    TutorInfo userToTutorInfo(User user, TutorSignupRequest tutorSignupRequest);

    /**
     * 경력 설명 리스트를 Experience 엔티티 리스트로 변환
     *
     * @param user 저장된 유저
     * @param descriptions 경력 설명 리스트
     * @return Experience 리스트
     */
    default List<Experience> toExperiences(User user, List<String> descriptions) {
        if (descriptions == null) {
            return null;
        }

        return descriptions.stream()
                .map(description -> Experience.builder()
                        .description(description)
                        .tutorInfo(user.getTutorInfo())
                        .build())
                .toList();
    }

    /**
     * 튜터 검색 페이지 응답 Dto 생성
     *
     * @param tutor 튜터 정보
     * @param userLanguageList 튜터 언어 리스트
     * @param tutorAgeGroupList 튜터 연령대 리스트
     * @return 튜터 검색 페이지 응답
     */
    @Mapping(target = "languages", source = "userLanguageList")
    @Mapping(target = "ageGroups", source = "tutorAgeGroupList" )
    TutorSearchPageResponse toTutorSearchPageResponse(User tutor, List<SearchLanguagesResponse> userLanguageList, List<SearchAgeGroupResponse> tutorAgeGroupList);


    /**
     * 튜터 검색 응답 Dto 생성
     *
     * @param tutor 튜터 정보
     * @param tutorInfo 튜터 정보
     * @param userLanguageList 튜터 언어 리스트
     * @param ageGroupList 튜터 연령대 리스트
     * @param experienceList 튜터 경력 리스트
     * @return 튜터 검색 응답
     */
    @Mapping(target = "languages", source = "userLanguageList")
    @Mapping(target = "ageGroups", source = "ageGroupList" )
    @Mapping(target = "experiences", source = "experienceList")
    @Mapping(target = "id", source = "tutor.id")
    TutorSearchResponse toTutorSearchResponse(User tutor, TutorInfo tutorInfo, List<SearchLanguagesResponse> userLanguageList, List<SearchAgeGroupResponse> ageGroupList, List<SearchExperienceResponse> experienceList);

    /**
     * 경력 조회 응답 dto 생성
     *
     * @param experience 경력 정보
     * @return 경력 조회 응답 dto
     */
    SearchExperienceResponse toSearchExperienceResponse(Experience experience);

}
