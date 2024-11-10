package inha.dayoook_e.tutor.domain.repository;

import inha.dayoook_e.tutor.domain.TutorApplication;
import inha.dayoook_e.tutor.domain.TutorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TutorApplicationJpaRepository는 TutorApplication 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface TutorApplicationJpaRepository extends JpaRepository<TutorApplication, Integer> {
}
