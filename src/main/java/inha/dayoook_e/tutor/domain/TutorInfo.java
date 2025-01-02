package inha.dayoook_e.tutor.domain;

import inha.dayoook_e.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 튜터 정보 엔티티.
 * 튜터의 정보를 담고 있음.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "tutor_info_tb")
public class TutorInfo implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private Integer userId;

    @Column(nullable = false, length = 255)
    private String introduction; // 자기소개

    @Column(nullable = false)
    private Double rating; // 평점

    @Column(name = "accept_at")
    private LocalDateTime acceptAt; // 튜터 승인 날짜

    @Column(nullable = false, length = 30)
    private String occupation; // 직업


    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "tutorInfo", fetch = FetchType.LAZY)
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "tutorInfo", fetch = FetchType.LAZY)
    private List<TutorAgeGroup> tutorAgeGroups = new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
