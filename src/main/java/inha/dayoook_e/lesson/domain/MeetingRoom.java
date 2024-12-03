package inha.dayoook_e.lesson.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "meeting_room_tb")
public class MeetingRoom {

    @Id
    @Column(name = "meeting_room_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_url", nullable = false, length = 100)
    private String roomUrl; // 회의실 URL

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 회의실 생성 날짜

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_schedule_id", nullable = false)
    private LessonSchedule lessonSchedule;

    public void setLessonSchedule(LessonSchedule lessonSchedule) {
        this.lessonSchedule = lessonSchedule;
    }
}
