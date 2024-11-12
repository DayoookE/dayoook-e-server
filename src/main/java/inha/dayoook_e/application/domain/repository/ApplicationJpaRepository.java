package inha.dayoook_e.application.domain.repository;

import inha.dayoook_e.application.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ApplicationJpaRepository는 Application 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface ApplicationJpaRepository extends JpaRepository<Application, Integer> {
}