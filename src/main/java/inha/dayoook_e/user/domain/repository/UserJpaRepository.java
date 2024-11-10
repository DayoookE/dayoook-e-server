package inha.dayoook_e.user.domain.repository;


import inha.dayoook_e.common.BaseEntity.State;
import inha.dayoook_e.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * UserJpaRepository는 User 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface UserJpaRepository extends JpaRepository<User, Integer> {

    /**
     * 사용자명과 상태를 기반으로 사용자를 찾음.
     *
     * @param email 찾으려는 사용자의 email
     * @param state 찾으려는 사용자의 상태 (ACTIVE 또는 INACTIVE)
     * @return 조건에 맞는 사용자를 포함하는 Optional 객체
     */
    Optional<User> findByEmailAndState(String email, State state);

    /**
     * 사용자 email과 상태를 기반으로 사용자가 존재하는지 확인.
     *
     * @param email 찾으려는 사용자의 email
     * @param state 찾으려는 사용자의 상태 (ACTIVE 또는 INACTIVE)
     * @return 사용자가 존재하면 true, 존재하지 않으면 false
     */
    boolean existsByEmailAndState(String email, State state);

    Optional<User> findByIdAndState(Integer id, State state);
}
