package inha.dayoook_e.application.api.service;

import inha.dayoook_e.application.api.controller.dto.request.ApplyRequest;
import inha.dayoook_e.application.api.controller.dto.request.TimeSlotRequest;
import inha.dayoook_e.application.api.controller.dto.response.ApplicationResponse;
import inha.dayoook_e.application.api.mapper.ApplicationMapper;
import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.application.domain.repository.ApplicationGroupJpaRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static inha.dayoook_e.application.domain.enums.Status.*;
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
    private final ApplicationGroupJpaRepository applicationGroupJpaRepository;
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

        // 2. 튜터 권한 검증
        if (!tutor.getRole().equals(Role.TUTOR)) {
            throw new BaseException(INVALID_ROLE);
        }

        // 3. 요청된 시간대들의 요일ID와 시간대ID Set 생성
        Set<Integer> requestedDayIds = applyRequest.timeSlots().stream()
                .map(TimeSlotRequest::dayId)
                .collect(Collectors.toSet());
        Set<Integer> requestedTimeSlotIds = applyRequest.timeSlots().stream()
                .map(TimeSlotRequest::timeSlotId)
                .collect(Collectors.toSet());

        // 4. 요일과 시간대 정보 한 번에 조회
        Map<Integer, Day> dayMap = dayJpaRepository.findAllById(requestedDayIds).stream()
                .collect(Collectors.toMap(Day::getId, day -> day));
        Map<Integer, TimeSlot> timeSlotMap = timeSlotJpaRepository.findAllById(requestedTimeSlotIds).stream()
                .collect(Collectors.toMap(TimeSlot::getId, timeSlot -> timeSlot));

        // 5. 요청된 모든 요일과 시간대가 존재하는지 검증
        validateRequestedIds(requestedDayIds, dayMap.keySet(), requestedTimeSlotIds, timeSlotMap.keySet());

        // 6. 시간대 중복 및 신청 중복 검증
        List<Application> allActiveApplications = applicationJpaRepository.findAll().stream()
                .filter(app ->
                        (app.getApplicationGroup().getStatus() == APPLYING ||
                                app.getApplicationGroup().getStatus() == APPROVED)
                )
                .collect(Collectors.toList());

        validateNoDuplicateApplications(tutee, tutor, applyRequest.timeSlots(), allActiveApplications, dayMap, timeSlotMap);

        // 7. 요청된 모든 시간대에 대한 TutorSchedule 조회 또는 생성
        List<TutorSchedule> tutorSchedules = new ArrayList<>();
        for (TimeSlotRequest timeSlotRequest : applyRequest.timeSlots()) {
            TutorScheduleId scheduleId = new TutorScheduleId(
                    applyRequest.tutorId(),
                    timeSlotRequest.dayId(),
                    timeSlotRequest.timeSlotId()
            );
            TutorSchedule tutorSchedule = tutorScheduleJpaRepository.findById(scheduleId)
                    .orElseGet(() -> {
                        Day day = dayMap.get(timeSlotRequest.dayId());
                        TimeSlot timeSlot = timeSlotMap.get(timeSlotRequest.timeSlotId());
                        TutorSchedule newTutorSchedule = tutorScheduleMapper.toTutorSchedule(
                                tutor, day, timeSlot, scheduleId, true
                        );
                        return tutorScheduleJpaRepository.save(newTutorSchedule);
                    });

            // 8. 튜터 스케줄 가용성 검증
            if (!tutorSchedule.getIsAvailable()) {
                throw new BaseException(SCHEDULE_NOT_AVAILABLE);
            }

            tutorSchedules.add(tutorSchedule);
        }

        // 9. ApplicationGroup 생성
        ApplicationGroup applicationGroup = ApplicationGroup.builder()
                .tutee(tutee)
                .tutor(tutor)
                .message(applyRequest.message())
                .status(APPLYING)
                .build();
        applicationGroup = applicationGroupJpaRepository.save(applicationGroup);

        // 10. 각 시간대별 Application 생성 및 그룹에 연결
        List<Application> applications = new ArrayList<>();
        for (TimeSlotRequest timeSlotRequest : applyRequest.timeSlots()) {
            Day day = dayMap.get(timeSlotRequest.dayId());
            TimeSlot timeSlot = timeSlotMap.get(timeSlotRequest.timeSlotId());

            Application application = Application.builder()
                    .tutee(tutee)
                    .tutor(tutor)
                    .day(day)
                    .timeSlot(timeSlot)
                    .status(APPLYING)
                    .applicationGroup(applicationGroup)  // ApplicationGroup 연결 추가
                    .applicationAt(LocalDateTime.now()) // 신청 시간 설정
                    .build();
            applications.add(application);
        }

        applicationJpaRepository.saveAll(applications);

        // 11. ApplicationGroup 기반 응답 생성
        return applicationMapper.toApplicationResponse(applicationGroup);
    }



    /**
     * 강의 신청 승인
     *
     * @param tutor 로그인 한 튜터
     * @param applicationGroupId 승인할 신청 그룹 id
     * @return 신청 승인 결과
     */
    @Override
    public ApplicationResponse approveApplication(User tutor, Integer applicationGroupId) {
        // 1. tutor의 권한이 tutor인지 확인
        if (!tutor.getRole().equals(Role.TUTOR)) {
            throw new BaseException(INVALID_ROLE);
        }

        // 2. ApplicationGroup 조회
        ApplicationGroup applicationGroup = applicationGroupJpaRepository.findById(applicationGroupId)
                .orElseThrow(() -> new BaseException(APPLICATION_GROUP_NOT_FOUND));

        // 3. ApplicationGroup의 튜터와 로그인한 튜터가 일치하는지 확인
        if (!applicationGroup.getTutor().getId().equals(tutor.getId())) {
            throw new BaseException(INVALID_ROLE);
        }

        // 4. ApplicationGroup의 상태가 APPLYING인지 확인
        if (applicationGroup.getStatus() != APPLYING) {
            throw new BaseException(INVALID_APPLICATION_STATUS);
        }

        // 5. 모든 시간대에 대해 스케줄 가용성 확인
        for (Application application : applicationGroup.getApplications()) {
            TutorScheduleId scheduleId = new TutorScheduleId(
                    tutor.getId(),
                    application.getDay().getId(),
                    application.getTimeSlot().getId()
            );

            TutorSchedule tutorSchedule = tutorScheduleJpaRepository.findById(scheduleId)
                    .orElseThrow(() -> new BaseException(SCHEDULE_NOT_FOUND));

            if (!tutorSchedule.getIsAvailable()) {
                throw new BaseException(SCHEDULE_ALREADY_BOOKED);
            }
        }

        // 6. 모든 검증이 통과되면, 각 Application에 대해 처리
        for (Application application : applicationGroup.getApplications()) {
            // 6.1. TutorSchedule 업데이트
            TutorScheduleId scheduleId = new TutorScheduleId(
                    tutor.getId(),
                    application.getDay().getId(),
                    application.getTimeSlot().getId()
            );
            TutorSchedule tutorSchedule = tutorScheduleJpaRepository.findById(scheduleId).get();
            tutorSchedule.makeUnavailable();

            // 6.2. Course 생성
            CreateCourseRequest createCourseRequest = courseMapper.toCreateCourseRequest(application);
            CourseResponse courseResponse = courseService.createCourse(createCourseRequest);
            log.info("Course 생성 성공, Course ID : {}", courseResponse.id());

            // 6.3. Application 상태 변경
            application.changeStatus(APPROVED);
        }

        // 7. ApplicationGroup 상태 변경
        applicationGroup.changeStatus(APPROVED);

        // 8. 결과 반환
        return applicationMapper.toApplicationResponse(applicationGroup);
    }

    /**
     * 강의 신청 거절
     *
     * @param tutor 로그인 한 튜터
     * @param applicationGroupId 거절할 신청 그룹 id
     * @return 신청 거절 결과
     */
    @Override
    public ApplicationResponse rejectApplication(User tutor, Integer applicationGroupId) {
        // 1. tutor의 권한이 tutor인지 확인
        if (!tutor.getRole().equals(Role.TUTOR)) {
            throw new BaseException(INVALID_ROLE);
        }

        // 2. ApplicationGroup 조회
        ApplicationGroup applicationGroup = applicationGroupJpaRepository.findById(applicationGroupId)
                .orElseThrow(() -> new BaseException(APPLICATION_GROUP_NOT_FOUND));

        // 3. ApplicationGroup의 튜터와 로그인한 튜터가 일치하는지 확인
        if (!applicationGroup.getTutor().getId().equals(tutor.getId())) {
            throw new BaseException(INVALID_ROLE);
        }

        // 4. ApplicationGroup의 상태가 APPLYING인지 확인
        if (applicationGroup.getStatus() != APPLYING) {
            throw new BaseException(INVALID_APPLICATION_STATUS);
        }

        // 5. 모든 Application의 상태를 REJECTED로 변경
        for (Application application : applicationGroup.getApplications()) {
            application.changeStatus(Status.REJECTED);
        }

        // 6. ApplicationGroup 상태 변경
        applicationGroup.changeStatus(Status.REJECTED);

        // 7. 결과 반환
        return applicationMapper.toApplicationResponse(applicationGroup);
    }

    /**
     * 중복 신청 검증 메서드
     *
     * @param tutee 신청자
     * @param tutor 튜터
     * @param timeSlotRequests 요청된 시간대들
     * @param allActiveApplications 활성화된 모든 신청 내역
     * @param dayMap 요일 정보
     * @param timeSlotMap 시간대 정보
     */
    private void validateNoDuplicateApplications(
            User tutee,
            User tutor,
            List<TimeSlotRequest> timeSlotRequests,
            List<Application> allActiveApplications,
            Map<Integer, Day> dayMap,
            Map<Integer, TimeSlot> timeSlotMap
    ) {
        // 동일 튜터에게 이미 승인/신청 중인 같은 시간대 신청 확인
        for (TimeSlotRequest request : timeSlotRequests) {
            Day day = dayMap.get(request.dayId());
            TimeSlot timeSlot = timeSlotMap.get(request.timeSlotId());

            // 동일 튜터의 같은 시간대 신청 확인
            boolean isDuplicateForTutor = allActiveApplications.stream()
                    .anyMatch(app ->
                            app.getTutor().getId().equals(tutor.getId()) &&
                                    app.getDay().equals(day) &&
                                    app.getTimeSlot().equals(timeSlot) &&
                                    (app.getApplicationGroup().getStatus() == APPLYING ||
                                            app.getApplicationGroup().getStatus() == APPROVED)
                    );

            if (isDuplicateForTutor) {
                throw new BaseException(DUPLICATE_TIMESLOT);
            }

            // 기존에 신청/승인된 다른 튜터의 같은 시간대 신청 확인
            boolean isDuplicateAcrossTutors = allActiveApplications.stream()
                    .anyMatch(app ->
                            app.getDay().equals(day) &&
                                    app.getTimeSlot().equals(timeSlot) &&
                                    (app.getApplicationGroup().getStatus() == APPLYING ||
                                            app.getApplicationGroup().getStatus() == APPROVED)
                    );

            if (isDuplicateAcrossTutors) {
                throw new BaseException(TIMESLOT_ALREADY_BOOKED);
            }
        }

        // 동일 튜터에 대한 재신청 확인
        boolean isDuplicateApplicationGroup = allActiveApplications.stream()
                .anyMatch(app ->
                        app.getTutor().getId().equals(tutor.getId()) &&
                                app.getTutee().getId().equals(tutee.getId()) &&
                                (app.getApplicationGroup().getStatus() == APPLYING ||
                                        app.getApplicationGroup().getStatus() == APPROVED)
                );

        if (isDuplicateApplicationGroup) {
            throw new BaseException(DUPLICATE_APPLICATION);
        }
    }

    /**
     * 요청된 요일과 시간대 ID들이 존재하는지 검증
     *
     * @param requestedDayIds 요청된 요일 ID들
     * @param foundDayIds 존재하는 요일 ID들
     * @param requestedTimeSlotIds 요청된 시간대 ID들
     * @param foundTimeSlotIds 존재하는 시간대 ID들
     */
    private void validateRequestedIds(
            Set<Integer> requestedDayIds,
            Set<Integer> foundDayIds,
            Set<Integer> requestedTimeSlotIds,
            Set<Integer> foundTimeSlotIds
    ) {
        if (!foundDayIds.containsAll(requestedDayIds)) {
            throw new BaseException(INVALID_DAY_ID);
        }
        if (!foundTimeSlotIds.containsAll(requestedTimeSlotIds)) {
            throw new BaseException(INVALID_TIME_SLOT_ID);
        }
    }
}
