package inha.dayoook_e.user.api.controller;

import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.user.api.controller.dto.request.TuteeSignupRequest;
import inha.dayoook_e.user.api.controller.dto.request.TutorSignupRequest;
import inha.dayoook_e.user.api.controller.dto.response.SignupResponse;
import inha.dayoook_e.user.api.controller.dto.response.UserInfoResponse;
import inha.dayoook_e.user.api.service.UserService;
import inha.dayoook_e.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static inha.dayoook_e.common.code.status.SuccessStatus.*;


/**
 * UserController은 유저 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "user controller", description = "유저 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * fast api 검증용 유저 정보 조회 API
     *
     * <p>유저 정보를 조회.</p>
     *
     * @param user 유저 정보
     *
     * @return 유저 정보를 포함하는 BaseResponse<UserInfoResponse>
     */
    @GetMapping("/info")
    @Operation(summary = "유저 정보 조회 API", description = "유저 정보를 조회합니다.")
    public BaseResponse<UserInfoResponse> getUserInfo(@AuthenticationPrincipal User user) {
        return BaseResponse.of(USER_INFO_OK, userService.getUserInfo(user));
    }

    /**
     * 튜티 회원가입 API
     *
     * <p>튜티 회원가입을 처리.</p>
     *
     * @param tuteeSignupRequest 튜티 회원가입 요청
     * @param profileImage 프로필 이미지
     * @return 튜티 회원가입 결과를 포함하는 BaseResponse<SignupResponse>
     */
    @PostMapping(value =  "/tutee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "튜티 회원가입 API", description = "튜티 회원가입을 처리합니다.")
    public BaseResponse<SignupResponse> tuteeSignup(@Validated  @RequestPart("tutee") TuteeSignupRequest tuteeSignupRequest,
                                                    @RequestPart(required = false, value = "profile") MultipartFile profileImage) {
        return BaseResponse.of(TUTEE_SIGNUP_OK, userService.tuteeSignup(tuteeSignupRequest, profileImage));
    }

    /**
     * 튜터 회원가입 API
     *
     * <p>튜터 회원가입을 처리.</p>
     *
     * @param tutorSignupRequest 튜터 회원가입 요청
     * @param profileImage 프로필 이미지
     * @return 튜터 회원가입 결과를 포함하는 BaseResponse<SignupResponse>
     */
    @PostMapping(value = "/tutor", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "튜터 회원가입 API", description = "튜터 회원가입을 처리합니다.")
    public BaseResponse<SignupResponse> tutorSignup(@Validated @RequestPart("tutor") TutorSignupRequest tutorSignupRequest,
                                                    @RequestPart(required = false, value = "profile") MultipartFile profileImage) {
        return BaseResponse.of(TUTOR_SIGNUP_OK, userService.tutorSignup(tutorSignupRequest, profileImage));
    }

}
