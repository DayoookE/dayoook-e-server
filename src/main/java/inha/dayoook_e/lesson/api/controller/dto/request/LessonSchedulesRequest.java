package inha.dayoook_e.lesson.api.controller.dto.request;

import java.util.List;

public record LessonSchedulesRequest(

        List<Integer> lessonScheduleIds
) {
}
