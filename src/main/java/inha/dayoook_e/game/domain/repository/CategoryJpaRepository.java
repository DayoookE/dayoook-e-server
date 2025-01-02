package inha.dayoook_e.game.domain.repository;


import inha.dayoook_e.game.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CategoryJpaRepository는 Category 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface CategoryJpaRepository extends JpaRepository<Category, Integer> {
}
