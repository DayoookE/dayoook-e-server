package inha.dayoook_e.tutor.domain.repository;

import inha.dayoook_e.tutor.domain.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ExperienceRepository는 Experience 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface ExperienceJpaRepository extends JpaRepository<Experience, Integer> {
}
