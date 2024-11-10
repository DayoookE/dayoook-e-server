package inha.dayoook_e.storybook.api.controller;

import inha.dayoook_e.storybook.api.service.StorybookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * StorybookController은 동화 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "storybook controller", description = "동화 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/storybooks")
public class StorybookController {

    private final StorybookService storybookService;


}
