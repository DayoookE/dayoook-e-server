package inha.dayoook_e.game.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Category 엔티티는 게임 카테고리에 대한 정보.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "category_tb")
public class Category {

    @Id
    @Column(name = "category_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20)
    private String name; // 카테고리 이름

}
