package inha.dayoook_e.lesson.api.service;

import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonRequest;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonResponse;

public interface LessonService {

    LessonResponse createLesson(CreateLessonRequest createLessonRequest);
}
