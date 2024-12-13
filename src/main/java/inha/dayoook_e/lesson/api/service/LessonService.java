package inha.dayoook_e.lesson.api.service;

import inha.dayoook_e.lesson.api.controller.dto.request.*;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonResponse;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonScheduleResponse;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonSchedulesResponse;
import inha.dayoook_e.user.domain.User;

import java.util.List;

public interface LessonService {

    LessonResponse createLesson(CreateLessonRequest createLessonRequest);
    LessonScheduleResponse createLessonSchedule(User user, String accessToken, CreateLessonScheduleRequest createLessonScheduleRequest);

    LessonScheduleResponse completeLessonSchedule(User user, Integer scheduleId, CompleteLessonRequest completeLessonRequest);
    LessonScheduleResponse cancelLessonSchedule(User user, Integer scheduleId, CancelLessonRequest cancelLessonRequest);
    List<LessonSchedulesResponse> getLessonSchedules(User user, LessonSchedulesRequest lessonSchedulesRequest);

}
