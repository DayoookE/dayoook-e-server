package inha.dayoook_e.auth.api.controller;

import inha.dayoook_e.auth.api.service.AuthService;
import inha.dayoook_e.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



/**
 * AuthController는 인증 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "auth controller", description = "인증 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;


    /**
     * 서버 상태 확인 API
     *
     * <p>서버 상태를 확인하는 API입니다.</p>
     *
     * @return 서버 상태를 포함하는 BaseResponse<String>
     */
    @GetMapping("/health")
    @Operation(summary = "서버 상태 확인 API", description = "서버 상태를 확인하는 API입니다.")
    public BaseResponse<String> healthCheck() {
        return BaseResponse.onSuccess("Server is running");
    }





}
