package inha.dayoook_e.storybook.domain;

import inha.dayoook_e.common.BaseEntity;
import inha.dayoook_e.mapping.domain.Country;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Storybook 엔티티는 동화 정보를 나타냄
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "storybook_tb")
public class Storybook extends BaseEntity {

    @Id
    @Column(name = "storybook_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String title; // 제목

    @Column(nullable = false, length = 200)
    private String description; // 설명

    @Column(name = "thumbnail_url", nullable = false, length = 100)
    private String thumbnailUrl; // 섬네일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country; // 국가

    @Column(name = "page_count", nullable = false)
    private Integer pageCount; // 페이지 수

    @Builder.Default
    @OneToMany(mappedBy = "storybook", fetch = FetchType.LAZY)
    private List<StorybookPage> pages = new ArrayList<>();

    public void addPage(StorybookPage page) {
        this.pages.add(page);
        page.setStorybook(this);
    }
}
