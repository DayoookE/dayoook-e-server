package inha.dayoook_e.lesson.domain.repository;

import inha.dayoook_e.lesson.domain.Lesson;
import inha.dayoook_e.lesson.domain.LessonSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

/**
 * LessonScheduleJpaRepository는 LessonSchedule 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface LessonScheduleJpaRepository extends JpaRepository<LessonSchedule, Integer> {
    boolean existsByLessonAndStartAt(Lesson lesson, LocalDateTime nextClassTime);
}
