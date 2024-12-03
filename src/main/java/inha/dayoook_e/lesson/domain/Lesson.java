package inha.dayoook_e.lesson.domain;


import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.common.BaseEntity;
import inha.dayoook_e.lesson.domain.enums.Status;
import inha.dayoook_e.mapping.domain.Day;
import inha.dayoook_e.mapping.domain.TimeSlot;
import inha.dayoook_e.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lesson 강의 엔티티
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "lesson_tb")
public class Lesson extends BaseEntity {

    @Id
    @Column(name = "lesson_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_group_id", nullable = false)
    private ApplicationGroup applicationGroup;

    @OneToMany(mappedBy = "lesson")
    private List<LessonSchedule> lessonSchedules = new ArrayList<>();

    public double calculateAttendanceRate() {
        long completedLessons = lessonSchedules.stream()
                .filter(schedule -> schedule.getStatus() == Status.COMPLETED)
                .count();

        if (completedLessons == 0) {
            return 0.0;
        }

        long attendedLessons = lessonSchedules.stream()
                .filter(schedule -> schedule.getStatus() == Status.COMPLETED && schedule.getAttendance())
                .count();

        return (double) attendedLessons / completedLessons * 100;
    }

}
