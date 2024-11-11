package inha.dayoook_e.storybook.domain;

import inha.dayoook_e.storybook.domain.id.TuteeStoryProgressId;
import inha.dayoook_e.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * TuteeStoryProgress 엔티티는 튜티의 스토리북 진행 정보를 나타냄
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "tutee_story_progress_tb")
public class TuteeStoryProgress {

    @EmbeddedId
    private TuteeStoryProgressId id;


    @Column(name = "liked", nullable = false)
    private Boolean liked; // 좋아요 여부

    @Column(name = "last_page_number", nullable = false)
    private Integer lastPageNumber; // 마지막 페이지 번호

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted; // 다 읽은 여부

    @MapsId("tuteeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id", nullable = false)
    private User tutee;

    @MapsId("storybookId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storybook_id", nullable = false)
    private Storybook storybook;

    public void toggleLike() {
        this.liked = !this.liked;
    }

    public void completeStory() {
        this.isCompleted = true;
    }

    public void updateLastReadPage(Integer pageNumber) {
        this.lastPageNumber = pageNumber;
    }
}
