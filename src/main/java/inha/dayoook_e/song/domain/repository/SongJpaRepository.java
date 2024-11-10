package inha.dayoook_e.song.domain.repository;


import inha.dayoook_e.common.BaseEntity.State;
import inha.dayoook_e.song.domain.Song;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * SongJpaRepository는 Song 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface SongJpaRepository extends JpaRepository<Song, Integer> {
    Slice<Song> findAllByCountry_IdAndState(Integer countryId, Pageable pageable, State state);

    Optional<Song> findByIdAndCountry_IdAndState(Integer id, Integer countryId, State state);
}
