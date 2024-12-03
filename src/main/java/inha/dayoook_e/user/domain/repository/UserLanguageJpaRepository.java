package inha.dayoook_e.user.domain.repository;


import inha.dayoook_e.common.BaseEntity;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.UserLanguage;
import inha.dayoook_e.user.domain.id.UserLanguageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;


/**
 * UserLanguageJpaRepository는 UserLanguage 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface UserLanguageJpaRepository extends JpaRepository<UserLanguage, UserLanguageId> {

    @Query("SELECT u FROM UserLanguage u WHERE u.user.id = :userId")
    List<UserLanguage> findByUserId(@Param("userId") Integer userId);


}
