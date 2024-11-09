package inha.dayoook_e.mapping.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "country_tb")
public class Country {

    @Id
    @Column(name = "country_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 30)
    private String name; // 국가 이름

    @Column(name = "flag_url", nullable = false, length = 100)
    private String flagUrl; // 국기 URL
}
