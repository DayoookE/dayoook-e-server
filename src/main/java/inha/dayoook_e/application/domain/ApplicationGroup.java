package inha.dayoook_e.application.domain;

import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.common.BaseEntity;
import inha.dayoook_e.lesson.domain.Lesson;
import inha.dayoook_e.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "application_group_tb")
public class ApplicationGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_group_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id", nullable = false)
    private User tutee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private User tutor;

    private String message;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "applicationGroup")
    private List<Application> applications = new ArrayList<>();

    public void changeStatus(Status status) {
        this.status = status;
    }

    @OneToOne(mappedBy = "applicationGroup", fetch = FetchType.LAZY)
    private Lesson lesson;

    // Optional을 반환하는 메서드 추가
    public Optional<Lesson> getLesson() {
        return Optional.ofNullable(lesson);
    }
}
