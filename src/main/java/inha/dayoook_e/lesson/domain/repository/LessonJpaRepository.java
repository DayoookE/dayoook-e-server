package inha.dayoook_e.lesson.domain.repository;

import inha.dayoook_e.common.BaseEntity;
import inha.dayoook_e.common.BaseEntity.State;
import inha.dayoook_e.lesson.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * LessonJpaRepository는 Lesson 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface LessonJpaRepository extends JpaRepository<Lesson, Integer> {

    Optional<Lesson> findByIdAndState(Integer id, State state);

    List<Lesson> findAllByApplicationGroup_Tutee_IdAndState(Integer tuteeId, State state);

    Optional<Lesson> findByApplicationGroupIdAndState(Integer id, State state);
}
