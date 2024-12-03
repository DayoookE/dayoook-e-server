package inha.dayoook_e.user.api.service;

import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.lesson.domain.Lesson;
import inha.dayoook_e.lesson.domain.LessonSchedule;
import inha.dayoook_e.lesson.domain.enums.Status;
import inha.dayoook_e.lesson.domain.repository.LessonJpaRepository;
import inha.dayoook_e.lesson.domain.repository.LessonScheduleJpaRepository;
import inha.dayoook_e.mapping.domain.Language;
import inha.dayoook_e.mapping.domain.repository.LanguageJpaRepository;
import inha.dayoook_e.song.domain.repository.SongJpaRepository;
import inha.dayoook_e.song.domain.repository.TuteeSongProgressJpaRepository;
import inha.dayoook_e.tutee.api.mapper.TuteeMapper;
import inha.dayoook_e.tutee.domain.TuteeInfo;
import inha.dayoook_e.tutee.domain.repository.TuteeInfoJpaRepository;
import inha.dayoook_e.tutor.api.mapper.TutorMapper;
import inha.dayoook_e.tutor.domain.TutorInfo;
import inha.dayoook_e.tutor.domain.repository.ExperienceJpaRepository;
import inha.dayoook_e.tutor.domain.repository.TutorInfoJpaRepository;
import inha.dayoook_e.user.api.controller.dto.request.TuteeSignupRequest;
import inha.dayoook_e.user.api.controller.dto.request.TutorSignupRequest;
import inha.dayoook_e.user.api.controller.dto.response.SignupResponse;
import inha.dayoook_e.user.api.controller.dto.response.UpcomingLessonInfo;
import inha.dayoook_e.user.api.controller.dto.response.UserInfoResponse;
import inha.dayoook_e.user.api.mapper.UserMapper;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.UserLanguage;
import inha.dayoook_e.user.domain.enums.Role;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import inha.dayoook_e.user.domain.repository.UserLanguageJpaRepository;
import inha.dayoook_e.utils.s3.S3Provider;
import inha.dayoook_e.utils.s3.dto.request.S3UploadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.common.Constant.PROFILE_IMAGE_DIR;
import static inha.dayoook_e.common.code.status.ErrorStatus.NOT_FIND_USER;
import static inha.dayoook_e.lesson.domain.enums.Status.*;
import static inha.dayoook_e.user.domain.enums.Role.*;

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
    private final TutorInfoJpaRepository tutorInfoJpaRepository;
    private final ExperienceJpaRepository experienceJpaRepository;
    private final UserLanguageJpaRepository userLanguageJpaRepository;
    private final LanguageJpaRepository languageJpaRepository;
    private final LessonScheduleJpaRepository lessonScheduleJpaRepository;
    private final SongJpaRepository songJpaRepository;
    private final LessonJpaRepository lessonJpaRepository;
    private final TuteeSongProgressJpaRepository tuteeSongProgressJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TuteeMapper tuteeMapper;
    private final TutorMapper tutorMapper;
    private final S3Provider s3Provider;
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
        User savedUser = userJpaRepository.save(user);

        // 2. 프로필 이미지 처리
        String profileUrl = handleProfileImage(profileImage, savedUser.getId());
        user.setProfileUrl(profileUrl);

        // 3. TuteeInfo 생성 및 저장
        TuteeInfo tuteeInfo = tuteeMapper.userToTuteeInfo(savedUser);
        tuteeInfo.setUser(savedUser);
        tuteeInfoJpaRepository.save(tuteeInfo);

        // 4. 사용자 언어 정보 저장
        List<Language> languages = languageJpaRepository.findAllByIdIn(tuteeSignupRequest.languageIdList());
        List<UserLanguage> userLanguages = userMapper.toUserLanguages(
                tuteeSignupRequest.languageIdList(),
                savedUser,
                languages
        );
        userLanguageJpaRepository.saveAll(userLanguages);

        return userMapper.userToSignupResponse(savedUser);
    }

    /**
     * 튜터 회원가입
     *
     * @param tutorSignupRequest 튜터 회원가입 요청
     * @param profileImage 프로필 이미지
     * @return 회원가입 응답
     */
    @Override
    public SignupResponse tutorSignup(TutorSignupRequest tutorSignupRequest, MultipartFile profileImage) {
        // 1. User 엔티티 생성
        User user = userMapper.usertoTutorSignupRequest(tutorSignupRequest);
        user.setPassword(passwordEncoder.encode(tutorSignupRequest.password()));
        User savedUser = userJpaRepository.save(user);

        // 2. 프로필 이미지 처리
        if(profileImage != null) {
            String profileUrl = handleProfileImage(profileImage, savedUser.getId() );
            user.setProfileUrl(profileUrl);
        }

        // 3. TutorInfo 생성 및 저장
        TutorInfo tutorInfo = tutorMapper.userToTutorInfo(savedUser, tutorSignupRequest);
        tutorInfo.setUser(savedUser);
        tutorInfoJpaRepository.save(tutorInfo);

        // 4. 사용자 언어 정보 저장
        List<Language> languages = languageJpaRepository.findAllByIdIn(tutorSignupRequest.languageIdList());
        List<UserLanguage> userLanguages = userMapper.toUserLanguages(
                tutorSignupRequest.languageIdList(),
                savedUser,
                languages
        );
        userLanguageJpaRepository.saveAll(userLanguages);

        // 5. 튜터 경력 정보 저장
        experienceJpaRepository.saveAll(tutorMapper.toExperiences(savedUser, tutorSignupRequest.descriptionList()));
        return userMapper.userToSignupResponse(savedUser);
    }

    /**
     * 사용자 정보 조회
     *
     * @param user 사용자 정보
     * @return 사용자 정보 응답
     */
    @Override
    public UserInfoResponse getUserInfo(User user) {
        if(user.getRole().equals(TUTEE)) {
            TuteeInfo tuteeInfo = tuteeInfoJpaRepository.findByuserId(user.getId())
                    .orElseThrow(() -> new BaseException(NOT_FIND_USER));

            // 출석률 계산
            Double attendanceRate = calculateTuteeAttendanceRate(user.getId());

            // 다음 예정된 수업 정보 조회
            UpcomingLessonInfo upcomingLesson = getUpcomingLesson(user.getId());

            return userMapper.userToTuteeInfoResponse(user, tuteeInfo, attendanceRate, upcomingLesson);
        }
        else {
            return userMapper.userToTutorInfoResponse(user);
        }
    }


    /**
     * 프로필 이미지 처리
     *
     * @param profileImage 프로필 이미지
     * @return S3 URL
     */
    private String handleProfileImage(MultipartFile profileImage, Integer userId) {
        return s3Provider.multipartFileUpload(profileImage, new S3UploadRequest(userId, PROFILE_IMAGE_DIR));
    }


    private Double calculateTuteeAttendanceRate(Integer tuteeId) {
        // 튜티의 모든 레슨 조회
        List<Lesson> lessons = lessonJpaRepository
                .findAllByApplicationGroup_Tutee_IdAndState(tuteeId, ACTIVE);

        if (lessons.isEmpty()) {
            return 0.0;
        }

        // 각 레슨의 출석률 계산 후 평균 내기
        double totalAttendanceRate = lessons.stream()
                .mapToDouble(Lesson::calculateAttendanceRate)
                .average()
                .orElse(0.0);

        return totalAttendanceRate;
    }

    private UpcomingLessonInfo getUpcomingLesson(Integer tuteeId) {
        // 가장 높은 ID값을 가진(=가장 최근) SCHEDULED 상태의 수업 찾기
        LessonSchedule upcomingSchedule = lessonScheduleJpaRepository
                .findFirstByLesson_ApplicationGroup_Tutee_IdAndStatusOrderByIdDesc(
                        tuteeId,
                        Status.SCHEDULED
                ).orElse(null);

        if (upcomingSchedule == null) {
            return null;
        }

        ApplicationGroup applicationGroup = upcomingSchedule.getLesson().getApplicationGroup();
        User tutor = applicationGroup.getTutor();

        return new UpcomingLessonInfo(
                tutor.getId(),
                tutor.getName(),
                upcomingSchedule.getStartAt()
        );
    }
}
