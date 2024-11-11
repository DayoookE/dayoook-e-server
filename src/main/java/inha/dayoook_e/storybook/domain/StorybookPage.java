package inha.dayoook_e.storybook.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * StorybookPage는 동화 페이지 엔티티.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "storybook_page_tb")
public class StorybookPage {

    @Id
    @Column(name = "storybook_page_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber; // 페이지 번호

    @Column(name = "page_url", nullable = false, length = 100)
    private String pageUrl; // 페이지 그림 URL

    @Column(nullable = false)
    private String content; // 페이지 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storybook_id")
    private Storybook storybook; // 동화

    public void setStorybook(Storybook storybook) {
        this.storybook = storybook;
    }


}
