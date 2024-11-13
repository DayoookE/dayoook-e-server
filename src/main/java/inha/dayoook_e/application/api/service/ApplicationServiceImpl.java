package inha.dayoook_e.application.api.service;

import inha.dayoook_e.application.api.controller.dto.request.ApplyRequest;
import inha.dayoook_e.application.api.controller.dto.response.ApplicationResponse;
import inha.dayoook_e.application.api.mapper.ApplicationMapper;
import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.course.api.controller.dto.request.CreateCourseRequest;
import inha.dayoook_e.course.api.controller.dto.response.CourseResponse;
import inha.dayoook_e.course.api.mapper.CourseMapper;
import inha.dayoook_e.course.api.service.CourseService;
import inha.dayoook_e.course.domain.Course;
import inha.dayoook_e.mapping.domain.Day;
import inha.dayoook_e.mapping.domain.TimeSlot;
import inha.dayoook_e.mapping.domain.repository.DayJpaRepository;
import inha.dayoook_e.mapping.domain.repository.TimeSlotJpaRepository;
import inha.dayoook_e.tutor.api.mapper.TutorScheduleMapper;
import inha.dayoook_e.tutor.domain.TutorSchedule;
import inha.dayoook_e.application.domain.repository.ApplicationJpaRepository;
import inha.dayoook_e.tutor.domain.id.TutorScheduleId;
import inha.dayoook_e.tutor.domain.repository.TutorScheduleJpaRepository;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.enums.Role;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.common.code.status.ErrorStatus.*;

/**
 * ApplicationServiceImpl은 강의 신청 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ApplicationServiceImpl implements ApplicationService{

    private final ApplicationJpaRepository applicationJpaRepository;
    private final TutorScheduleJpaRepository tutorScheduleJpaRepository;
    private final  DayJpaRepository dayJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ApplicationMapper applicationMapper;
    private final TutorScheduleMapper tutorScheduleMapper;
    private final CourseService courseService;
    private final CourseMapper courseMapper;

    /**
     * 강의 신청
     *
     * @param tutee 로그인한 사용자
     * @param applyRequest 강의 개설 신청
     * @return 강의 신청 생성 결과
     */
    public ApplicationResponse apply(User tutee, ApplyRequest applyRequest) {
        // 1. 튜터 조회
        User tutor = userJpaRepository.findByIdAndState(applyRequest.tutorId(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        // 2. 튜터 권한 조회
        if (!tutor.getRole().equals(Role.TUTOR))
            throw new BaseException(INVALID_ROLE);

        // 3. 요일 조회
        Day day = dayJpaRepository.findById(applyRequest.dayId())
                .orElseThrow(() -> new BaseException(NOT_FIND_DAY));

        // 4. 시간대 조회
        TimeSlot timeSlot = timeSlotJpaRepository.findById(applyRequest.timeSlotId())
                .orElseThrow(() -> new BaseException(NOT_FIND_TIMESLOT));

        // 5. 튜터 스케줄 조회, 없으면 생성
        TutorScheduleId scheduleId = new TutorScheduleId(applyRequest.tutorId(), applyRequest.dayId(), applyRequest.timeSlotId());
        TutorSchedule tutorSchedule = tutorScheduleJpaRepository.findById(scheduleId)
                .orElseGet(() -> {
                    TutorSchedule newTutorSchedule = tutorScheduleMapper.toTutorSchedule(tutor, day, timeSlot, scheduleId, true);
                    return tutorScheduleJpaRepository.save(newTutorSchedule);
                });


        // 6-1. 스케줄이 available 하다면 신청 생성
        if (tutorSchedule.getIsAvailable()) {
            Application application = applicationMapper.toApplication(tutee, tutor, day, timeSlot,
                    LocalDateTime.now(), Status.APPLYING, applyRequest.message());
            Application savedApplication = applicationJpaRepository.save(application);
            return applicationMapper.applicationToApplicationResponse(savedApplication);
        }
        // 6-2. 스케줄이 unavailable 하다면 오류 반환
        else {
            throw new BaseException(SCHEDULE_ALREADY_BOOKED);
        }
    }

    /**
     * 강의 신청 승인
     *
     * @param tutor 로그인 한 튜터
     * @param applicationId 승인할 신청 id
     * @return 신청 승인 결과
     */
    @Override
    public ApplicationResponse approveApplication(User tutor, Integer applicationId) {
        // 1. tutor의 권한이 tutor인지 확인
        if (!tutor.getRole().equals(Role.TUTOR))
            throw new BaseException(INVALID_ROLE);

        // 2. application 조회
        Application application = applicationJpaRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(APPLICATION_NOT_FOUND));


        // 2. 튜터 스케줄 조회, 없으면 오류 반환
        TutorScheduleId scheduleId = new TutorScheduleId(application.getTutor().getId(), application.getDay().getId(), application.getTimeSlot().getId());
        TutorSchedule tutorSchedule = tutorScheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new BaseException(SCHEDULE_NOT_FOUND));

        // 3. 스케줄이 available 하다면 신청 승인
        if (tutorSchedule.getIsAvailable()) {

            // 3-1. 스케쥴 isAvailable 변경
            tutorSchedule.makeUnavailable();

            // 3-2. application status 변경
            application.changeStatus(Status.APPROVED);

            // 3-2. Course 생성
            CreateCourseRequest createCourseRequest = courseMapper.toCreateCourseRequest(application);
            CourseResponse courseResponse = courseService.createCourse(createCourseRequest);
            log.info("Course 생성 성공, Course ID : {}", courseResponse.id());

            // 3-3. Application response 반환
            return applicationMapper.applicationToApplicationResponse(application);
        }
        // 4. 스케줄이 unavailable 하다면 오류 반환
        else {
            throw new BaseException(SCHEDULE_ALREADY_BOOKED);
        }
    }

    /**
     * 강의 신청 거절
     *
     * @param tutor 로그인 한 튜터
     * @param applicationId 거절할 신청 id
     * @return 신청 거절 결과
     */
    @Override
    public ApplicationResponse rejectApplication(User tutor, Integer applicationId) {
        // 1. tutor의 권한이 tutor인지 확인
        if (!tutor.getRole().equals(Role.TUTOR))
            throw new BaseException(INVALID_ROLE);

        // 2. application 조회
        Application application = applicationJpaRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(APPLICATION_NOT_FOUND));

        // 3. 신청 거절 (거절에는 이유가 필요 없음)
        application.changeStatus(Status.REJECTED);

        // 4. application Reponse반환
        return applicationMapper.applicationToApplicationResponse(application);
    }
}
