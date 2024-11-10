package inha.dayoook_e.game.domain;

import inha.dayoook_e.game.domain.id.TuteeGameProgressId;
import inha.dayoook_e.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * TuteeGameProgress 엔티티는 튜티의 게임 진행 상태에 대한 정보.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "tutee_game_progress_tb")
public class TuteeGameProgress {

    @EmbeddedId
    private TuteeGameProgressId id;

    @Column(nullable = false)
    private Boolean corrected; // 정답 여부

    @Column(name = "wrong_count", nullable = false)
    private Integer wrongCount; // 틀린 횟수

    @MapsId("tuteeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id", nullable = false)
    private User tutee;

    @MapsId("gameId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
}
