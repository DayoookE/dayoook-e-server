package inha.dayoook_e.song.domain;

import inha.dayoook_e.common.BaseEntity;
import inha.dayoook_e.mapping.domain.Country;
import jakarta.persistence.*;
import lombok.*;

/**
 * Song은 동요 엔티티.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "song_tb")
public class Song extends BaseEntity {

    @Id
    @Column(name = "song_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String title; // 제목

    @Column(nullable = false, length = 200)
    private String description; // 설명

    @Column(name = "thumbnail_url", nullable = false, length = 100)
    private String thumbnailUrl; // 섬네일

    @Column(name = "media_url", nullable = false, length = 100)
    private String mediaUrl; // 음원 URL

    @Column(nullable = false)
    private String lyrics; // 가사

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country; // 국가
}
