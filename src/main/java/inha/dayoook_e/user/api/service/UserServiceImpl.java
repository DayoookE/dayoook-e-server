package inha.dayoook_e.user.api.service;

import inha.dayoook_e.mapping.domain.Language;
import inha.dayoook_e.mapping.domain.repository.LanguageJpaRepository;
import inha.dayoook_e.tutee.api.mapper.TuteeMapper;
import inha.dayoook_e.tutee.domain.TuteeInfo;
import inha.dayoook_e.tutee.domain.repository.TuteeInfoJpaRepository;
import inha.dayoook_e.user.api.controller.dto.request.TuteeSignupRequest;
import inha.dayoook_e.user.api.controller.dto.response.SignupResponse;
import inha.dayoook_e.user.api.mapper.UserMapper;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.UserLanguage;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import inha.dayoook_e.user.domain.repository.UserLanguageJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;

/**
 * UserServiceImpl은 유저 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {


    private final UserJpaRepository userJpaRepository;
    private final TuteeInfoJpaRepository tuteeInfoJpaRepository;
    private final UserLanguageJpaRepository userLanguageJpaRepository;
    private final LanguageJpaRepository languageJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TuteeMapper tuteeMapper;

    /**
     * 튜티 회원가입
     *
     * @param tuteeSignupRequest 튜티 회원가입 요청
     * @param profileImage 프로필 이미지
     * @return 회원가입 응답
     */
    @Override
    public SignupResponse tuteeSignup(TuteeSignupRequest tuteeSignupRequest, MultipartFile profileImage) {
        // 1. User 엔티티 생성
        User user = userMapper.usertoTuteeSignupRequest(tuteeSignupRequest);
        user.setPassword(passwordEncoder.encode(tuteeSignupRequest.password()));

        // 프로필 이미지 처리
        String profileUrl = handleProfileImage(profileImage);
        user.setProfileUrl(profileUrl);
        // 2. User 저장
        User savedUser = userJpaRepository.save(user);

        // 3. TuteeInfo 생성 및 저장
        TuteeInfo tuteeInfo = tuteeMapper.userToTuteeInfo(savedUser);
        tuteeInfo.setUser(savedUser);
        tuteeInfoJpaRepository.save(tuteeInfo);

        // 4. 사용자 언어 정보 저장
        List<Language> languages = languageJpaRepository.findAllByIdInAndState(tuteeSignupRequest.languageIdList(), ACTIVE);
        List<UserLanguage> userLanguages = userMapper.toUserLanguages(
                tuteeSignupRequest.languageIdList(),
                savedUser,
                languages
        );
        userLanguageJpaRepository.saveAll(userLanguages);

        return userMapper.userToSignupResponse(savedUser);
    }


    private String handleProfileImage(MultipartFile profileImage) {
        // TODO: 실제 이미지 업로드 로직 구현
        // 임시로 더미 URL 반환
        return "default-profile-url";
    }
}
