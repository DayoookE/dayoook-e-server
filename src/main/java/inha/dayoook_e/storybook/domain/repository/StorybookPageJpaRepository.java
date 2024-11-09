package inha.dayoook_e.storybook.domain.repository;

import inha.dayoook_e.storybook.domain.StorybookPage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * StorybookPageJpaRepository는 StorybookPage 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface StorybookPageJpaRepository extends JpaRepository<StorybookPage, Integer> {
}
