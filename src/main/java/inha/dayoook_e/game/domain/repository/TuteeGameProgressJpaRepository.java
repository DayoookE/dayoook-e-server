package inha.dayoook_e.game.domain.repository;


import inha.dayoook_e.game.domain.TuteeGameProgress;
import inha.dayoook_e.game.domain.id.TuteeGameProgressId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TuteeGameProgressJpaRepository는 TuteeGameProgress 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface TuteeGameProgressJpaRepository extends JpaRepository<TuteeGameProgress, TuteeGameProgressId> {
}
