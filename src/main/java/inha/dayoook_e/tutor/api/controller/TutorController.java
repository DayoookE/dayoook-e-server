package inha.dayoook_e.tutor.api.controller;

import inha.dayoook_e.tutor.api.service.TutorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



/**
 * TutorController는 튜터 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "tutor controller", description = "튜터 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tutors")
public class TutorController {

    private final TutorService tutorService;



}
