package inha.dayoook_e.tutee.domain.enums;

/**
 * Level 열거형은 사용자 레벨을 정의.
 * 각 레벨은 해당 레벨에 부여된 권한 세트를 가짐.
 */
public enum Level {

    SEEDLING("새싹"),
    STEM("줄기"),
    LEAF("잎"),
    FLOWER("꽃"),
    FRUIT("열매");

    private final String level;

    Level(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}