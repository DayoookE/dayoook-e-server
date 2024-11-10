package inha.dayoook_e.song.api.controller;

import inha.dayoook_e.song.api.service.SongService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * SongController은 동요 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "song controller", description = "동요 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/songs")
public class SongController {

    private final SongService songService;


}
