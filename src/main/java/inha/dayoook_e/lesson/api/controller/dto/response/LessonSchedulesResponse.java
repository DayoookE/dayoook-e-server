package inha.dayoook_e.lesson.api.controller.dto.response;

import inha.dayoook_e.tutor.api.controller.dto.response.SearchTutorScheduleResponse;

import java.time.LocalDateTime;

public record LessonSchedulesResponse(

        Integer lessonId,

        Integer lessonScheduleId,

        SearchTutorScheduleResponse tutorSchedule,

        LocalDateTime createdAt
) {
}
