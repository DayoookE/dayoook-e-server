package inha.dayoook_e.mapping.domain;

import inha.dayoook_e.user.domain.UserLanguage;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Language 엔티티는 언어 정보를 저장
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "language_tb")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id", nullable = false, updatable = false)
    private Integer id;

    @Column(nullable = false, length = 20)
    private String name; // 언어명

    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
    private List<UserLanguage> userLanguages = new ArrayList<>();

}
