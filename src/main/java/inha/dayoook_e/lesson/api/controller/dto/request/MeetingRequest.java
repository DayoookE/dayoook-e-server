package inha.dayoook_e.lesson.api.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record MeetingRequest(

        @NotNull
        String tutor_email
) {
}
