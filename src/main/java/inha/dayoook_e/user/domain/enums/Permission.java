package inha.dayoook_e.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Permission 열거형은 사용자 권한을 정의.
 * 각 권한은 특정 작업에 대한 액세스 권한을 나타냄.
 */
@RequiredArgsConstructor
public enum Permission {

    //관리자
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    // 튜터
    TUTOR_READ("tutor:read"),
    TUTOR_UPDATE("tutor:update"),
    TUTOR_CREATE("tutor:create"),
    TUTOR_DELETE("tutor:delete");

    @Getter
    private final String permission;
}
