package inha.dayoook_e.lesson.api.mapper;

import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonRequest;
import inha.dayoook_e.lesson.api.controller.dto.request.MeetingRequest;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonResponse;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonScheduleResponse;
import inha.dayoook_e.lesson.domain.Lesson;
import inha.dayoook_e.lesson.domain.LessonSchedule;
import inha.dayoook_e.lesson.domain.MeetingRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;

/**
 * LessonMapper는 게임과 관련된 데이터 변환 기능을 제공.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LessonMapper {

    /**
     * Application을 CreateLessonRequest로 변환해주는 매퍼
     *
     * @param applicationGroup 등록할 신청 정보
     * @return createCourseRequest
     */
    CreateLessonRequest toCreateLessonRequest(ApplicationGroup applicationGroup);

    /**
     * Lesson를 LessonResponse로 변환하는 매퍼
     *
     * @param lesson 강의
     * @return LessonResponse
     */
    LessonResponse toLessonResponse(Lesson lesson);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicationGroup", source = "applicationGroup")
    Lesson toLesson(ApplicationGroup applicationGroup);

    /**
     * Lesson, MeetingRoom, LocalDateTime을 LessonSchedule로 변환하는 매퍼
     *
     * @param lesson      강의
     * @param meetingRoom 회의실
     * @param startAt     시작 시간
     * @return LessonSchedule
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attendance", constant = "false")
    @Mapping(target = "status", constant = "SCHEDULED")
    LessonSchedule toLessonSchedule(Lesson lesson, MeetingRoom meetingRoom, LocalDateTime startAt);

    /**
     * LessonSchedule을 LessonScheduleResponse로 변환하는 매퍼
     *
     * @param schedule    수업 일정
     * @param meetingRoom 회의실
     * @return LessonScheduleResponse
     */
    @Mapping(target = "id", source = "schedule.id")
    @Mapping(target = "status", source = "schedule.status")
    @Mapping(target = "attendance", source = "schedule.attendance")
    @Mapping(target = "startAt", source = "schedule.startAt")
    @Mapping(target = "roomUrl", source = "meetingRoom.roomUrl")
    LessonScheduleResponse toLessonScheduleResponse(LessonSchedule schedule, MeetingRoom meetingRoom);

    MeetingRequest toMeetingRequest(String tutor_email, String tutee_email);
}