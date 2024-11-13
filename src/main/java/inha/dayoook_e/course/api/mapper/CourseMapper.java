package inha.dayoook_e.course.api.mapper;

import inha.dayoook_e.application.api.controller.dto.response.ApplicationResponse;
import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.course.api.controller.dto.request.CreateCourseRequest;
import inha.dayoook_e.course.api.controller.dto.response.CourseResponse;
import inha.dayoook_e.course.domain.Course;
import inha.dayoook_e.mapping.domain.Day;
import inha.dayoook_e.mapping.domain.TimeSlot;
import inha.dayoook_e.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CourseMapper {

    default CreateCourseRequest toCreateCourseRequest(Application application) {
        return new CreateCourseRequest(application.getTutor().getId(), application.getTutee().getId(),
                application.getDay().getId(), application.getTimeSlot().getId());
    };

    CourseResponse toCourseResponse(Course savedCourse);
}
