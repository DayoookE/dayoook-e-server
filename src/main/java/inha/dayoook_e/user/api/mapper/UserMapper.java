package inha.dayoook_e.user.api.mapper;

import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.mapping.domain.Language;
import inha.dayoook_e.user.api.controller.dto.request.TuteeSignupRequest;
import inha.dayoook_e.user.api.controller.dto.response.SignupResponse;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.UserLanguage;
import inha.dayoook_e.user.domain.id.UserLanguageId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

import static inha.dayoook_e.common.code.status.ErrorStatus.INVALID_LANGUAGE_ID;

/**
 * UserMapper은 유저와 관련된 데이터 변환 기능을 제공.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    /**
     * TuteeSignupRequest를 User 엔티티로 변환
     *
     * @param tuteeSignupRequest 튜티 회원가입 요청
     * @return User 엔티티
     */
    @Mapping(target = "role", constant = "TUTEE")
    User usertoTuteeSignupRequest(TuteeSignupRequest tuteeSignupRequest);


    /**
     * 언어 ID 리스트와 User를 UserLanguage 리스트로 변환
     *
     * @param languageIds 언어 ID 리스트
     * @param user 유저 엔티티
     * @param languages 언어 엔티티 리스트
     * @return UserLanguage 리스트
     */
    default List<UserLanguage> toUserLanguages(List<Integer> languageIds, User user, List<Language> languages) {
        if (languageIds == null) {
            return null;
        }
        return languageIds.stream()
                .map(languageId -> {
                    Language language = languages.stream()
                            .filter(l -> l.getId().equals(languageId))
                            .findFirst()
                            .orElseThrow(() -> new BaseException(INVALID_LANGUAGE_ID));

                    return UserLanguage.builder()
                            .id(new UserLanguageId(user.getId(), languageId))
                            .user(user)
                            .language(language)
                            .build();
                })
                .toList();
    }

    /**
     * User를 SignupResponse로 변환
     *
     * @param savedUser 저장된 유저
     * @return SignupResponse
     */
    SignupResponse userToSignupResponse(User savedUser);
}
