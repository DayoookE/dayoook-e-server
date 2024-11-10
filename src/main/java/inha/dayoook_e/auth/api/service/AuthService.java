package inha.dayoook_e.auth.api.service;

import inha.dayoook_e.auth.api.controller.dto.request.LoginRequest;
import inha.dayoook_e.auth.api.controller.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);
}
