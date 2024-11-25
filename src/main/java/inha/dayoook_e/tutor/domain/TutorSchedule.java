package inha.dayoook_e.tutor.domain;

import inha.dayoook_e.mapping.domain.AgeGroup;
import inha.dayoook_e.mapping.domain.Day;
import inha.dayoook_e.mapping.domain.TimeSlot;
import inha.dayoook_e.tutor.domain.id.TutorAgeGroupId;
import inha.dayoook_e.tutor.domain.id.TutorScheduleId;
import inha.dayoook_e.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * TutorAgeGroup 엔티티는 튜터가 지도 가능한 주연령층 정보를 나타냄
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "tutor_schedule_tb")
public class TutorSchedule {

    @EmbeddedId
    private TutorScheduleId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("dayId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_id", nullable = false)
    private Day day;

    @MapsId("timeSlotId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", nullable = false)
    private TimeSlot timeSlot;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable; // 튜터가 해당 시간에 수업 가능한지 여부

    public void makeUnavailable() {
        this.isAvailable = false;
    }

    public void makeAvailable() {
        this.isAvailable = true;
    }
}
