package inha.dayoook_e.tutor.domain.repository;

import inha.dayoook_e.mapping.domain.Day;
import inha.dayoook_e.mapping.domain.TimeSlot;
import inha.dayoook_e.tutor.domain.TutorSchedule;
import inha.dayoook_e.tutor.domain.id.TutorScheduleId;
import inha.dayoook_e.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * TutorScheduleJpaRepository는 TutorSchedule 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface TutorScheduleJpaRepository extends JpaRepository<TutorSchedule, TutorScheduleId> {
    List<TutorSchedule> findByUserId(Integer id);

    List<TutorSchedule> findByUserIdAndIsAvailable(Integer tutorId, boolean isAvailable);
}
