package inha.dayoook_e.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


/**
 * 포인트 엔티티.
 * 유저의 포인트 정보를 담고 있음.
 * */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "point_tb")
public class Point {

    @Id
    @Column(name = "point_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer point; // 포인트

    private String reason; // 포인트 적립/차감 사유

    private LocalDateTime createdAt; // 포인트 적립/차감 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
