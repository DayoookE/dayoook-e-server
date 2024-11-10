package inha.dayoook_e.user.domain;

import inha.dayoook_e.common.BaseEntity;
import inha.dayoook_e.tutee.domain.TuteeInfo;
import inha.dayoook_e.tutor.domain.TutorInfo;
import inha.dayoook_e.user.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User 엔티티는 애플리케이션의 사용자 정보를 나타냄.
 * 이 클래스는 Spring Security의 UserDetails 인터페이스를 구현하여 사용자 인증 및 권한 부여에 사용.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "user_tb")
public class User extends BaseEntity implements UserDetails {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 30)
    private String email; // 이메일

    @Column(nullable = false, length = 100)
    private String password; // 비밀번호

    @Column(nullable = false, length = 12)
    private String name; // 이름

    @Column(nullable = false)
    private Integer age; // 나이

    @Column(name = "profile_url",nullable = false, length = 100)
    private String profileUrl; // 프로필 이미지 URL

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private TutorInfo tutorInfo;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private TuteeInfo tuteeInfo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Point> points = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserLanguage> userLanguages = new ArrayList<>();

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
