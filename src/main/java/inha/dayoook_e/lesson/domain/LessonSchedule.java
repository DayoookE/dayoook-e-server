package inha.dayoook_e.lesson.domain;

import inha.dayoook_e.lesson.domain.enums.Status;
import inha.dayoook_e.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

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

    //출석 여부
    @Column(nullable = false)
    private Boolean attendance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private User tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id", nullable = false)
    private User tutee;

    @OneToOne(mappedBy = "lessonSchedule", fetch = FetchType.LAZY)
    private MeetingRoom meetingRoom;
}
