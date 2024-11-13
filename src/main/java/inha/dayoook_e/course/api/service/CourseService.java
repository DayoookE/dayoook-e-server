package inha.dayoook_e.course.api.service;

import inha.dayoook_e.course.api.controller.dto.request.CreateCourseRequest;
import inha.dayoook_e.course.api.controller.dto.response.CourseResponse;

public interface CourseService {
    CourseResponse createCourse(CreateCourseRequest createCourseRequest);
}
