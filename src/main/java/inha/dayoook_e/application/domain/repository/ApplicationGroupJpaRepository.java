package inha.dayoook_e.application.domain.repository;

import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.common.BaseEntity;
import inha.dayoook_e.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * ApplicationGroupJpaRepository는 ApplicationGroup 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface ApplicationGroupJpaRepository extends JpaRepository<ApplicationGroup, Integer> {

    Page<ApplicationGroup> findByTutorAndStatus(User tutor, Status status, Pageable pageable);

    Page<ApplicationGroup> findByTutor(User tutor, Pageable pageable);

    Optional<ApplicationGroup> findByIdAndState(Integer integer, BaseEntity.State state);

    Slice<ApplicationGroup> findSliceByTuteeAndStatusAndState(User user, Status status, BaseEntity.State state, PageRequest pageRequest);

    Slice<ApplicationGroup> findSliceByTuteeAndState(User user, BaseEntity.State state, PageRequest pageRequest);

}
