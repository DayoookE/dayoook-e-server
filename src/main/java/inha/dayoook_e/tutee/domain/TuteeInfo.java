package inha.dayoook_e.tutee.domain;

import inha.dayoook_e.tutee.domain.enums.Level;
import inha.dayoook_e.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * 튜티 정보 엔티티.
 * 튜티의 정보를 담고 있음.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "tutee_info_tb")
public class TuteeInfo implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private Integer userId;

    @Column(nullable = false)
    private Integer point; // 포인트

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Level level; // 튜티 레벨

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
