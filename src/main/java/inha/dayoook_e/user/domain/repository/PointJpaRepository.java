package inha.dayoook_e.user.domain.repository;


import inha.dayoook_e.user.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * PointJpaRepository는 Point 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface PointJpaRepository extends JpaRepository<Point, Integer> {


}
