package inha.dayoook_e.mapping.domain;

import jakarta.persistence.*;
import lombok.*;


/**
 * Day 엔티티는 요일 정보를 나타냄
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "day_tb")
public class Day {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "day_id", nullable = false, updatable = false)
        private Integer id;

        @Column(nullable = false, length = 20)
        private String name; // 요일

}
