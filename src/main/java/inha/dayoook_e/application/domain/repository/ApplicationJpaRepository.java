package inha.dayoook_e.application.domain.repository;

import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * ApplicationJpaRepository는 Application 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface ApplicationJpaRepository extends JpaRepository<Application, Integer> {

    @Query("SELECT a FROM Application a WHERE a.tutor.id != :tutorId " +
            "AND a.applicationGroup.status IN :statuses")
    List<Application> findByTutorIdNotAndApplicationGroupStatusIn(
            @Param("tutorId") Integer tutorId,
            @Param("statuses") List<Status> statuses
    );
}
