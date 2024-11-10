package inha.dayoook_e.game.api.controller;

import inha.dayoook_e.game.api.service.GameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



/**
 * GameController는 게임 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "game controller", description = "게임 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/games")
public class GameController {

    private final GameService gameService;


}
