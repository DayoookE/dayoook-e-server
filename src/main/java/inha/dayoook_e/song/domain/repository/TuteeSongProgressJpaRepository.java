package inha.dayoook_e.song.domain.repository;


import inha.dayoook_e.song.domain.TuteeSongProgress;
import inha.dayoook_e.song.domain.id.TuteeSongProgressId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * TuteeSongProgressJpaRepository는 TuteeSongProgress 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface TuteeSongProgressJpaRepository extends JpaRepository<TuteeSongProgress, TuteeSongProgressId> {

    Optional<TuteeSongProgress> findByTutee_IdAndSong_Id(Integer tuteeId, Integer songId);
}
