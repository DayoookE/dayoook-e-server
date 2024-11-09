package inha.dayoook_e.tutor.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 튜터 경력을 나타내는 엔티티.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "experience_tb")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experience_id", nullable = false, updatable = false)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String description; // 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private TutorInfo tutorInfo;
}
