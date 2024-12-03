package inha.dayoook_e.lesson.domain.repository;

import inha.dayoook_e.lesson.domain.Lesson;
import inha.dayoook_e.lesson.domain.LessonSchedule;
import inha.dayoook_e.lesson.domain.enums.Status;
import inha.dayoook_e.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * LessonScheduleJpaRepository는 LessonSchedule 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface LessonScheduleJpaRepository extends JpaRepository<LessonSchedule, Integer> {
    boolean existsByLessonAndStartAt(Lesson lesson, LocalDateTime nextClassTime);


    Optional<LessonSchedule> findFirstByLesson_ApplicationGroup_Tutee_IdAndStatusOrderByIdDesc(
            Integer tuteeId,
            Status status
    );

    List<LessonSchedule> findAllByLesson_ApplicationGroup_TuteeAndStartAtBetween(
            User tutee,
            LocalDateTime startAt,
            LocalDateTime endAt
    );

}
