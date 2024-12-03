package inha.dayoook_e.lesson.domain;

import inha.dayoook_e.lesson.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * LessonSchedule은 수업 일정을 나타내는 엔티티.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "lesson_schedule_tb")
public class LessonSchedule {

    @Id
    @Column(name = "lesson_schedule_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Status status; // 수업 상태

    @Column(nullable = false)
    private Boolean attendance; //출석 여부

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt; // 수업 시작 시간

    @OneToOne(mappedBy = "lessonSchedule", fetch = FetchType.LAZY)
    private MeetingRoom meetingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Course_id", nullable = false)
    private Lesson lesson;


    public void complete() {
        this.attendance = Boolean.TRUE;
        this.status = Status.COMPLETED;
    }


}
