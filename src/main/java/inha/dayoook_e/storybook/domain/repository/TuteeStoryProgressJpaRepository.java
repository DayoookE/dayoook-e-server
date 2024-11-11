package inha.dayoook_e.storybook.domain.repository;

import inha.dayoook_e.storybook.domain.TuteeStoryProgress;
import inha.dayoook_e.storybook.domain.id.TuteeStoryProgressId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * TuteeStoryProgressJpaRepository는 TuteeStoryProgress 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface TuteeStoryProgressJpaRepository extends JpaRepository<TuteeStoryProgress, TuteeStoryProgressId> {
    Optional<TuteeStoryProgress> findByTutee_IdAndStorybook_Id(Integer id, Integer storybookId);
}
