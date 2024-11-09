package inha.dayoook_e.tutor.domain.enums;

public enum Status {

    // 신청중, 승인, 취소, 거절
    APPLYING("신청중"), APPROVED("승인"), CANCELED("취소"), REJECTED("거절");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
