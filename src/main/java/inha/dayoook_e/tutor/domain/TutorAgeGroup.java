package inha.dayoook_e.tutor.domain;

import inha.dayoook_e.mapping.domain.AgeGroup;
import inha.dayoook_e.tutor.domain.id.TutorAgeGroupId;
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
@Table(name = "tutor_age_group_tb")
public class TutorAgeGroup {

    @EmbeddedId
    private TutorAgeGroupId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private TutorInfo tutorInfo;

    @MapsId("ageGroupId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "age_group_id", nullable = false)
    private AgeGroup ageGroup;
}
