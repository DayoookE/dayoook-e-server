package inha.dayoook_e.application.api.mapper;

import inha.dayoook_e.application.api.controller.dto.request.ApplyRequest;
import inha.dayoook_e.application.api.controller.dto.response.ApplicationResponse;
import inha.dayoook_e.application.domain.Application;
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

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ApplicationMapper {
    ApplicationResponse applicationToApplicationResponse(Application application);

    @Mapping(target = "id", ignore = true)
    Application applyRequestToApplication(ApplyRequest applyRequest, User tutee, User tutor, Status status, LocalDateTime applicationAt, Day day, TimeSlot timeSlot);


    default TutorSchedule toTutorSchedule(User tutor, Day day, TimeSlot timeSlot, TutorScheduleId scheduleId) {
        return new TutorSchedule(scheduleId, tutor, day, timeSlot, true);
    }
}
