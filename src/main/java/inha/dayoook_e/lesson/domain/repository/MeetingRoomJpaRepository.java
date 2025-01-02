package inha.dayoook_e.lesson.domain.repository;

import inha.dayoook_e.lesson.domain.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MeetingRoomJpaRepository는 MeetingRoom 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface MeetingRoomJpaRepository extends JpaRepository<MeetingRoom, Integer> {
}
