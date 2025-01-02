package inha.dayoook_e.game.domain;

import inha.dayoook_e.mapping.domain.Country;
import jakarta.persistence.*;
import lombok.*;

/**
 * Game 엔티티는 게임에 대한 정보.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "game_tb")
public class Game {

    @Id
    @Column(name = "game_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String title; // 제목

    @Column(nullable = false, length = 200)
    private String description; // 설명

    @Column(name = "thumbnail_url", nullable = false, length = 100)
    private String thumbnailUrl; // 섬네일

    @Column(nullable = false, length = 200)
    private String answer; // 정답

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country; // 국가

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // 카테고리


}
