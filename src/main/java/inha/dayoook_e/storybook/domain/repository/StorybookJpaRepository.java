package inha.dayoook_e.storybook.domain.repository;

import inha.dayoook_e.common.BaseEntity.State;
import inha.dayoook_e.storybook.domain.Storybook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * StorybookJpaRepository는 Storybook 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface StorybookJpaRepository extends JpaRepository<Storybook, Integer> {
    Optional<Storybook> findByIdAndState(Integer storybookId, State state);
}
