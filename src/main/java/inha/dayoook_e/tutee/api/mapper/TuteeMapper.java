package inha.dayoook_e.tutee.api.mapper;

import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;
import inha.dayoook_e.tutee.api.controller.dto.response.SearchTuteeApplicationResponse;
import inha.dayoook_e.tutee.domain.TuteeInfo;
import inha.dayoook_e.tutor.api.controller.dto.request.ScheduleTimeSlot;
import inha.dayoook_e.user.api.controller.dto.response.TutorInfoResponse;
import inha.dayoook_e.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

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

    @Mapping(target = "id", source = "applicationGroup.id")
    @Mapping(target = "tutorInfo", source = "applicationGroup.tutor")
    @Mapping(target = "languages", source = "languages")
    @Mapping(target = "scheduleTimeSlots", source = "scheduleTimeSlots")
    @Mapping(target = "createdAt", source = "applicationGroup.createdAt")
    @Mapping(target = "status", source = "applicationGroup.status")
    @Mapping(target = "message", source = "applicationGroup.message")
    SearchTuteeApplicationResponse toSearchTuteeApplicationResponse(
            ApplicationGroup applicationGroup,
            List<SearchLanguagesResponse> languages,
            List<ScheduleTimeSlot> scheduleTimeSlots
    );

    @Mapping(target = "id", source = "tutor.id")
    @Mapping(target = "role", source = "tutor.role")
    @Mapping(target = "name", source = "tutor.name")
    @Mapping(target = "profileUrl", source = "tutor.profileUrl")
    TutorInfoResponse toTutorInfoResponse(User tutor);

}
