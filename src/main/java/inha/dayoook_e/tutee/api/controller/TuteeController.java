package inha.dayoook_e.tutee.api.controller;

import inha.dayoook_e.tutee.api.service.TuteeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



/**
 * TuteeController은 튜티 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "tutee controller", description = "튜티 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tutees")
public class TuteeController {

    private final TuteeService tuteeService;


}
