package inha.dayoook_e.tutor.domain.repository;

import inha.dayoook_e.tutor.domain.TutorAgeGroup;
import inha.dayoook_e.tutor.domain.id.TutorAgeGroupId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TutorAgeGroupJpaRepository는 TutorAgeGroup 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface TutorAgeGroupJpaRepository extends JpaRepository<TutorAgeGroup, TutorAgeGroupId> {
}
