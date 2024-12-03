package inha.dayoook_e.lesson.api.service;

import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonRequest;
import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonScheduleRequest;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonResponse;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonScheduleResponse;
import inha.dayoook_e.user.domain.User;

public interface LessonService {

    LessonResponse createLesson(CreateLessonRequest createLessonRequest);
    LessonScheduleResponse createLessonSchedule(User user, CreateLessonScheduleRequest createLessonScheduleRequest);
}
