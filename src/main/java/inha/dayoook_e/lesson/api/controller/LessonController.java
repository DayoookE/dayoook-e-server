package inha.dayoook_e.lesson.api.controller;

import inha.dayoook_e.lesson.api.service.LessonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



/**
 * LessonController는 교육 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "lesson controller", description = "교육 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {

    private final LessonService lessonService;


}
