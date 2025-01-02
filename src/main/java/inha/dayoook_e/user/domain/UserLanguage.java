package inha.dayoook_e.user.domain;

import inha.dayoook_e.mapping.domain.Language;
import inha.dayoook_e.user.domain.id.UserLanguageId;
import jakarta.persistence.*;
import lombok.*;

/**
 * UserLanguage 엔티티는 사용자가 구사 가능한 언어 정보를 나타냄
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
@Entity
@Table(name = "user_language_tb")
public class UserLanguage {


    @EmbeddedId
    private UserLanguageId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("languageId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;
}
