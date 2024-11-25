package inha.dayoook_e.application.domain;

import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.common.BaseEntity;
import inha.dayoook_e.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "application_group_tb")
public class ApplicationGroup extends BaseEntity {

    @Id
    @Column(name = "application_group_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
