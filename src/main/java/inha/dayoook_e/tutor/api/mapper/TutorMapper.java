package inha.dayoook_e.tutor.api.mapper;

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
}
