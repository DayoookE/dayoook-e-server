package inha.dayoook_e.tutor.domain.repository;

import inha.dayoook_e.tutor.domain.Experience;
import inha.dayoook_e.user.domain.UserLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * ExperienceRepository는 Experience 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface ExperienceJpaRepository extends JpaRepository<Experience, Integer> {
    @Query("SELECT u FROM Experience u WHERE u.tutorInfo.userId = :userId")
    List<Experience> findByUserId(@Param("userId") Integer userId);
}
