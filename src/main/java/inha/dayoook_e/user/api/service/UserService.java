package inha.dayoook_e.user.api.service;

import inha.dayoook_e.user.api.controller.dto.request.TuteeSignupRequest;
import inha.dayoook_e.user.api.controller.dto.request.TutorSignupRequest;
import inha.dayoook_e.user.api.controller.dto.response.SignupResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    SignupResponse tuteeSignup(TuteeSignupRequest tuteeSignupRequest, MultipartFile profileImage);

    SignupResponse tutorSignup(TutorSignupRequest tutorSignupRequest, MultipartFile profileImage);
}
