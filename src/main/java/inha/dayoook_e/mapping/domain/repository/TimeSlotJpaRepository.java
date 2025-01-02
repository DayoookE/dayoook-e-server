package inha.dayoook_e.mapping.domain.repository;

import inha.dayoook_e.mapping.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * TimeSlotJpaRepository는 TimeSlot 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface TimeSlotJpaRepository extends JpaRepository<TimeSlot, Integer> {
    List<TimeSlot> findByTime(String timeSlotName);
}
