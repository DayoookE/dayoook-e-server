package inha.dayoook_e.mapping.domain.repository;

import inha.dayoook_e.mapping.domain.AgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AgeGroupJpaRepository는 AgeGroup 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface AgeGroupJpaRepository extends JpaRepository<AgeGroup, Integer> {
}