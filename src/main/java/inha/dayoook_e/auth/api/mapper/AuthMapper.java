package inha.dayoook_e.auth.api.mapper;

import inha.dayoook_e.auth.api.controller.dto.response.LoginResponse;
import inha.dayoook_e.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * AuthMapper는 인증과 관련된 데이터 변환 기능을 제공.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthMapper {

    /**
     * User 엔티티를 LoginResponse로 변환
     *
     * @param user        회원 정보
     * @param accessToken  JWT Access Token
     * @return LoginResponse
     */
    @Mapping(target = "role", source = "user.role")
    LoginResponse userToLoginResponse(User user, String accessToken);
}
