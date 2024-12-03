package inha.dayoook_e.lesson.api.mapper;

import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonRequest;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonResponse;
import inha.dayoook_e.lesson.domain.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * LessonMapper는 게임과 관련된 데이터 변환 기능을 제공.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LessonMapper {

    /**
     * Application을 CreateLessonRequest로 변환해주는 매퍼
     * @param application 등록할 신청 정보
     * @return createCourseRequest
     */
    default CreateLessonRequest toCreateLessonRequest(Application application) {
        return new CreateLessonRequest(application.getTutor().getId(), application.getTutee().getId(),
                application.getDay().getId(), application.getTimeSlot().getId());
    };

    /**
     * Lesson를 LessonResponse로 변환하는 매퍼
     *
     * @param lesson 강의
     * @return LessonResponse
     */
    LessonResponse toLessonResponse(Lesson lesson);

}
