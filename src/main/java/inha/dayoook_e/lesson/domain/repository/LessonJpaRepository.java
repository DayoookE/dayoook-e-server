package inha.dayoook_e.lesson.domain.repository;

import inha.dayoook_e.lesson.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LessonJpaRepository는 Lesson 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface LessonJpaRepository extends JpaRepository<Lesson, Integer> {
}
