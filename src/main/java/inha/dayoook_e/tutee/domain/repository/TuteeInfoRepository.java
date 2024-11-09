package inha.dayoook_e.tutee.domain.repository;

import inha.dayoook_e.tutee.domain.TuteeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TuteeInfoRepository는 TuteeInfo 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface TuteeInfoRepository extends JpaRepository<TuteeInfo, Integer> {
}
