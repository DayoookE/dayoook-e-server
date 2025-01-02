package inha.dayoook_e.user.domain.enums;

public enum KoreanLevel {

    BEGINNER("초급"),
    INTERMEDIATE("중급"),
    ADVANCED("고급");

    private final String level;

    KoreanLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}
