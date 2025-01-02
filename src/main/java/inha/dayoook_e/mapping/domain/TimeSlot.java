package inha.dayoook_e.mapping.domain;

import jakarta.persistence.*;
import lombok.*;


/**
 * TimeSlot 엔티티는 시간대 정보를 나타냄
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "time_slot_tb")
public class TimeSlot {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "time_slot_id", nullable = false, updatable = false)
        private Integer id;

        @Column(nullable = false, length = 20)
        private String time; // 시간대

}
