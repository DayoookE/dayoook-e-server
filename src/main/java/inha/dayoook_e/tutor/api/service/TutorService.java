package inha.dayoook_e.tutor.api.service;

import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.tutor.api.controller.dto.request.SearchCond;
import inha.dayoook_e.tutor.api.controller.dto.request.TutorScheduleRequest;
import inha.dayoook_e.tutor.api.controller.dto.response.*;
import inha.dayoook_e.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;


public interface TutorService {
    Slice<TutorSearchPageResponse> getTutors(SearchCond searchCond, Integer page);
    TutorSearchResponse getTutor(Integer tutorId);
    TutorResponse createSchedule(User user, TutorScheduleRequest tutorScheduleRequest);
    SearchTutorScheduleResponse getTutorSchedule(User user, Integer tutorId);
    Page<SearchTutorApplicationResponse> getTutorApplication(User user, Integer tutorId, Integer page, Status status);
}
