package inha.dayoook_e.auth.api.service;

import inha.dayoook_e.auth.api.controller.dto.request.LoginRequest;
import inha.dayoook_e.auth.api.controller.dto.response.LoginResponse;
import inha.dayoook_e.auth.api.mapper.AuthMapper;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.tutor.domain.TutorInfo;
import inha.dayoook_e.tutor.domain.repository.TutorInfoJpaRepository;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import inha.dayoook_e.utils.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.common.code.status.ErrorStatus.NOT_APPROVED_USER;
import static inha.dayoook_e.common.code.status.ErrorStatus.NOT_FIND_USER;
import static inha.dayoook_e.user.domain.enums.Role.TUTOR;

/**
 * AuthServiceImpl은 인증 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserJpaRepository userJpaRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;
    private final TutorInfoJpaRepository tutorInfoJpaRepository;
    private final JwtProvider jwtProvider;

    /**
     * 로그인 API
     *
     * <p>로그인을 처리합니다.</p>
     *
     * @param loginRequest 로그인 요청 정보
     * @return 로그인 결과를 포함하는 LoginResponse
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("사용자 {} 로그인 시도!!!222", loginRequest.email());
        User findUser = userJpaRepository.findByEmailAndState(loginRequest.email(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        log.info("사용자 {} 로그인 시도!!!", loginRequest.email());
        try{
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        } catch (BadCredentialsException e) {
            log.error("로그인 실패 - 사용자: {}", loginRequest.email());
            throw new BaseException(NOT_FIND_USER);
        }
        if(findUser.getRole().equals(TUTOR)) {
            TutorInfo tutorInfo = tutorInfoJpaRepository.findById(findUser.getId())
                    .orElseThrow(() -> new BaseException(NOT_FIND_USER));
            if(tutorInfo.getAcceptAt() == null) {
                log.error("승인되지 않은 튜터 로그인 시도 - 사용자: {}", loginRequest.email());
                throw new BaseException(NOT_APPROVED_USER);
            }
        }
        String accessToken = jwtProvider.generateToken(findUser);
        log.info("사용자 {} 로그인 성공", findUser.getEmail());
        return authMapper.userToLoginResponse(findUser, accessToken);
    }
}
