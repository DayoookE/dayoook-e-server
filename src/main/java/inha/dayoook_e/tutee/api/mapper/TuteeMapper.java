package inha.dayoook_e.tutee.api.mapper;

import inha.dayoook_e.tutee.domain.TuteeInfo;
import inha.dayoook_e.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * TuteeMapper은 튜티와 관련된 데이터 변환 기능을 제공.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TuteeMapper {

    /**
     * User를 TuteeInfo 엔티티로 변환
     *
     * @param user 유저
     * @return TuteeInfo 엔티티
     */
    @Mapping(target = "point", constant = "0")
    @Mapping(target = "level", constant = "SEEDLING")
    TuteeInfo userToTuteeInfo(User user);
}
