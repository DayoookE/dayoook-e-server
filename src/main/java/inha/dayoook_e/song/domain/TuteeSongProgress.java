package inha.dayoook_e.song.domain;

import inha.dayoook_e.song.domain.id.TuteeSongProgressId;
import inha.dayoook_e.user.domain.User;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "tutee_song_progress_tb")
public class TuteeSongProgress {

    @EmbeddedId
    private TuteeSongProgressId id;

    @Column(name = "liked", nullable = false)
    private Boolean liked; // 좋아요 여부

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted; // 다 들은 여부

    @MapsId("tuteeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id", nullable = false)
    private User tutee;

    @MapsId("songId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

}
