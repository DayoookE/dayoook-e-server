package inha.dayoook_e.tutee.api.service;

import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.tutee.api.controller.dto.response.SearchTuteeApplicationResponse;
import inha.dayoook_e.tutee.api.controller.dto.response.TuteeScheduleResponse;
import inha.dayoook_e.user.domain.User;
import org.springframework.data.domain.Slice;

public interface TuteeService {
    Slice<SearchTuteeApplicationResponse> getTuteeApplications(User user, Integer page, Status status);
    TuteeScheduleResponse getTuteeSchedule(User user, Integer year, Integer month);
}
