package inha.dayoook_e.song.domain.repository;


import inha.dayoook_e.song.domain.TuteeSongProgress;
import inha.dayoook_e.song.domain.id.TuteeSongProgressId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * TuteeSongProgressJpaRepository는 TuteeSongProgress 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface TuteeSongProgressJpaRepository extends JpaRepository<TuteeSongProgress, TuteeSongProgressId> {

    @Query("SELECT tsp FROM TuteeSongProgress tsp " +
            "JOIN FETCH tsp.song " +
            "WHERE tsp.tutee.id = :tuteeId " +
            "AND tsp.song.id IN :songIds")
    List<TuteeSongProgress> findAllByTutee_IdAndSong_IdIn(
            @Param("tuteeId") Integer tuteeId,
            @Param("songIds") List<Integer> songIds
    );

    Optional<TuteeSongProgress> findByTutee_IdAndSong_Id(Integer tuteeId, Integer songId);
}
