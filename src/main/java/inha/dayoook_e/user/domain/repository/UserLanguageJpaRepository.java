package inha.dayoook_e.user.domain.repository;


import inha.dayoook_e.user.domain.UserLanguage;
import inha.dayoook_e.user.domain.id.UserLanguageId;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * UserLanguageJpaRepository는 UserLanguage 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface UserLanguageJpaRepository extends JpaRepository<UserLanguage, UserLanguageId> {


}
