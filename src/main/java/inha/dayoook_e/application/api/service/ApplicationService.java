package inha.dayoook_e.application.api.service;

import inha.dayoook_e.application.api.controller.dto.request.ApplyRequest;
import inha.dayoook_e.application.api.controller.dto.response.ApplicationResponse;
import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.user.domain.User;

public interface ApplicationService {
    ApplicationResponse apply(User user, ApplyRequest applyRequest);
    ApplicationResponse approveApplication(User tutor, Integer applicationGroupId);
    ApplicationResponse rejectApplication(User user, Integer applicationGroupId);
}
