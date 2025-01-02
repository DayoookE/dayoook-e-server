package inha.dayoook_e.lesson.domain.repository;

import inha.dayoook_e.lesson.domain.LessonRecording;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LessonRecordingJpaRepository는 LessonRecording 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface LessonRecordingJpaRepository extends JpaRepository<LessonRecording, Integer> {
}
