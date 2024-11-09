package inha.dayoook_e.lesson.domain.repository;

import inha.dayoook_e.lesson.domain.LessonReview;
import inha.dayoook_e.lesson.domain.LessonSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LessonReviewJpaRepository는 LessonReview 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface LessonReviewJpaRepository extends JpaRepository<LessonReview, Integer> {
}
