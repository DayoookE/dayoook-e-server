package inha.dayoook_e.application.domain.repository;

import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ApplicationJpaRepository는 Application 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface ApplicationJpaRepository extends JpaRepository<Application, Integer> {
    List<Application> findByTuteeAndTutor(User tutee, User tutor);

    List<Application> findByTuteeAndStatusIn(User tutee, List<Status> applying);
}
