package inha.dayoook_e.game.api.service;

import inha.dayoook_e.game.api.mapper.GameMapper;
import inha.dayoook_e.game.domain.repository.CategoryJpaRepository;
import inha.dayoook_e.game.domain.repository.GameJpaRepository;
import inha.dayoook_e.game.domain.repository.TuteeGameProgressJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * GameServiceImpl은 게임 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GameServiceImpl implements GameService {

    private final GameJpaRepository gameJpaRepository;
    private final TuteeGameProgressJpaRepository tuteeGameProgressJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final GameMapper gameMapper;
}
