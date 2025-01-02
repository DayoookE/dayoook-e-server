package inha.dayoook_e.lesson.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * LessonRecording은 수업 녹화 정보를 나타냄.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "lesson_record_tb")
public class LessonRecording {

    @Id
    @Column(name = "lesson_review_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "recording_url", nullable = false, length = 100)
    private String recordingUrl; // 녹화 URL

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt; // 녹화 날짜

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_schedule_id", nullable = false)
    private LessonSchedule lessonSchedule;
}
