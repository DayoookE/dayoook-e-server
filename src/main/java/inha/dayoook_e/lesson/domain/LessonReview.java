package inha.dayoook_e.lesson.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * LessonReview는 수업 복습을 나타냄.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "lesson_review_tb")
public class LessonReview {

    @Id
    @Column(name = "lesson_review_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "review_content", nullable = false, length = 255)
    private String reviewContent; //수업 내용 요약

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted; // 복습 완료 여부

    @Column(name = "completed_at")
    private LocalDateTime completedAt; // 복습 완료 날짜

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_schedule_id", nullable = false)
    private LessonSchedule lessonSchedule;
}
