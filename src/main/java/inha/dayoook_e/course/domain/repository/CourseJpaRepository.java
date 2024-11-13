package inha.dayoook_e.course.domain.repository;

import inha.dayoook_e.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CourseJpaRepository는 Course 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface CourseJpaRepository extends JpaRepository<Course, Integer> {
}
