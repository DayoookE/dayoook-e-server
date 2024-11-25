package inha.dayoook_e.application.api.mapper;

import inha.dayoook_e.application.api.controller.dto.request.ApplyRequest;
import inha.dayoook_e.application.api.controller.dto.response.ApplicationResponse;
import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.mapping.domain.Day;
import inha.dayoook_e.mapping.domain.TimeSlot;
import inha.dayoook_e.tutor.domain.TutorSchedule;
import inha.dayoook_e.tutor.domain.id.TutorScheduleId;
import inha.dayoook_e.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ApplicationMapper는 강의 신청과 관련된 데이터 변환 기능을 제공.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ApplicationMapper {

    /**
     * Application 엔티티를 ApplicationResponse로 변환
     *
     * @param application 신청 정보
     * @return ApplicationResponse
     */
    ApplicationResponse applicationToApplicationResponse(Application application);


    /**
     * Application 생성
     *
     * @param tutee 튜티 정보
     * @param tutor 튜터 정보
     * @param day 요일 정보
     * @param timeSlot 시간대 정보
     * @param applicationAt 신청 생성 시각
     * @param status 신청 상태
     * @param message 튜터에게 보내는 메시지
     * @return Application 엔티티
     */
    @Mapping(target = "id", ignore = true)
    Application toApplication(User tutee, User tutor, Day day, TimeSlot timeSlot,
                              LocalDateTime applicationAt, Status status, String message);


    @Mapping(target = "id", source = "applicationGroup.id")
    ApplicationResponse toApplicationResponse(ApplicationGroup applicationGroup);
}
