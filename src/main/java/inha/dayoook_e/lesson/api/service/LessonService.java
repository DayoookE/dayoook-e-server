package inha.dayoook_e.lesson.api.service;

import inha.dayoook_e.lesson.api.controller.dto.request.CancelLessonRequest;
import inha.dayoook_e.lesson.api.controller.dto.request.CompleteLessonRequest;
import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonRequest;
import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonScheduleRequest;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonResponse;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonScheduleResponse;
import inha.dayoook_e.user.domain.User;

public interface LessonService {

    LessonResponse createLesson(CreateLessonRequest createLessonRequest);
    LessonScheduleResponse createLessonSchedule(User user, CreateLessonScheduleRequest createLessonScheduleRequest);

    LessonScheduleResponse completeLessonSchedule(User user, Integer scheduleId, CompleteLessonRequest completeLessonRequest);
    LessonScheduleResponse cancelLessonSchedule(User user, Integer scheduleId, CancelLessonRequest cancelLessonRequest);

}
