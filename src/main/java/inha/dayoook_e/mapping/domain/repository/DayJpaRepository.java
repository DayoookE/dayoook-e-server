package inha.dayoook_e.mapping.domain.repository;

import inha.dayoook_e.mapping.domain.Day;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DayJpaRepository는 Day 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface DayJpaRepository extends JpaRepository<Day, Integer> {
}
