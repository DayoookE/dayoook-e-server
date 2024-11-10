package inha.dayoook_e.game.domain.repository;


import inha.dayoook_e.game.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * GameJpaRepository는 Game 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface GameJpaRepository extends JpaRepository<Game, Integer> {
}
