package inha.dayoook_e.lesson.domain.enums;

public enum Status {

    SCHEDULED("예정"), COMPLETED("완료"), CANCELED("취소"), NOT_CREATED("미생성");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
