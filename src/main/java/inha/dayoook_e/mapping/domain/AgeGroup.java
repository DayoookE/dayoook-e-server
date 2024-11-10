package inha.dayoook_e.mapping.domain;

import inha.dayoook_e.tutor.domain.TutorAgeGroup;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * AgeGroup 엔티티는 연령층 정보를 저장
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "age_group_tb")
public class AgeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "age_group_id", nullable = false, updatable = false)
    private Integer id;

    @Column(nullable = false, length = 20)
    private String name; // 연령층

    @OneToMany(mappedBy = "ageGroup", fetch = FetchType.LAZY)
    private List<TutorAgeGroup> tutorAgeGroups = new ArrayList<>();
}
