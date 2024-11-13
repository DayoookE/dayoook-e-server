package inha.dayoook_e.course.api.mapper;

import inha.dayoook_e.application.api.controller.dto.response.ApplicationResponse;
import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.course.api.controller.dto.request.CreateCourseRequest;
import inha.dayoook_e.course.api.controller.dto.response.CourseResponse;
import inha.dayoook_e.course.domain.Course;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * CourseMapper은 강의와 관련된 데이터 변환 기능을 제공.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CourseMapper {

    /**
     * Application을 CreateCourseRequest로 변환해주는 매퍼
     * @param application 등록할 신청 정보
     * @return createCourseRequest
     */
    default CreateCourseRequest toCreateCourseRequest(Application application) {
        return new CreateCourseRequest(application.getTutor().getId(), application.getTutee().getId(),
                application.getDay().getId(), application.getTimeSlot().getId());
    };

    /**
     * Course를 CourseResponse로 변환하는 매퍼
     *
     * @param savedCourse 강의
     * @return CourseResponse
     */
    CourseResponse toCourseResponse(Course savedCourse);
}
