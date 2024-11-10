package inha.dayoook_e.mapping.domain.repository;

import inha.dayoook_e.mapping.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * LanguageJpaRepository는 Language 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface LanguageJpaRepository extends JpaRepository<Language, Integer> {
    List<Language> findAllByIdIn(List<Integer> ids);

    Optional<Language> findByName(String languageName);

}
