package inha.dayoook_e.song.domain.repository;


import inha.dayoook_e.common.BaseEntity.State;
import inha.dayoook_e.song.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * SongJpaRepository는 Song 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface SongJpaRepository extends JpaRepository<Song, Integer> {

    Optional<Song> findByIdAndState(Integer id, State state);
}
