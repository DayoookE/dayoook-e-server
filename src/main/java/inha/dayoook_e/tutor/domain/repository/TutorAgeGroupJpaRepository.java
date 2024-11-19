package inha.dayoook_e.tutor.domain.repository;

import inha.dayoook_e.tutor.domain.Experience;
import inha.dayoook_e.tutor.domain.TutorAgeGroup;
import inha.dayoook_e.tutor.domain.id.TutorAgeGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * TutorAgeGroupJpaRepository는 TutorAgeGroup 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface TutorAgeGroupJpaRepository extends JpaRepository<TutorAgeGroup, TutorAgeGroupId> {

    @Query("SELECT u FROM TutorAgeGroup u WHERE u.tutorInfo.userId = :userId")
    List<TutorAgeGroup> findByUserId(@Param("userId") Integer userId);
}
