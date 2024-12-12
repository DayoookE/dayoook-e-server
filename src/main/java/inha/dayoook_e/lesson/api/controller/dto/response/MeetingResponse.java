package inha.dayoook_e.lesson.api.controller.dto.response;

public record MeetingResponse(
        String meeting_uri,
        String created_at,
        String status,
        String event_id
) { }