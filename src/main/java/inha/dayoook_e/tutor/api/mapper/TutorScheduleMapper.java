package inha.dayoook_e.tutor.api.mapper;

import inha.dayoook_e.mapping.domain.Day;
import inha.dayoook_e.mapping.domain.TimeSlot;
import inha.dayoook_e.tutor.domain.TutorInfo;
import inha.dayoook_e.tutor.domain.TutorSchedule;
import inha.dayoook_e.tutor.domain.id.TutorScheduleId;
import inha.dayoook_e.user.api.controller.dto.request.TutorSignupRequest;
import inha.dayoook_e.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * TutorScheduleMapper은 튜터 스케쥴과 관련된 데이터 변환 기능을 제공.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TutorScheduleMapper {

    /**
     * TutorSchedule 생성
     *
     * @param tutor 튜터 정보
     * @param day 요일 정보
     * @param timeSlot 시간대 정보
     * @param scheduleId 스케줄 정보 ID
     * @param isAvailable 신청 가능한 스케줄인지 판단
     * @return TutorSchedule 엔티티
     */
    @Mapping(target = "id", source = "scheduleId")
    TutorSchedule toTutorSchedule(User tutor, Day day, TimeSlot timeSlot, TutorScheduleId scheduleId,
                                  Boolean isAvailable);

}
