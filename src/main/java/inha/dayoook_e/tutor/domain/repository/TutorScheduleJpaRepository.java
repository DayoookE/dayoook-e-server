package inha.dayoook_e.tutor.domain.repository;

import inha.dayoook_e.tutor.domain.TutorSchedule;
import inha.dayoook_e.tutor.domain.id.TutorScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TutorScheduleJpaRepository는 TutorSchedule 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface TutorScheduleJpaRepository extends JpaRepository<TutorSchedule, TutorScheduleId> {
}
