package inha.dayoook_e.application.api.service;

import inha.dayoook_e.application.api.controller.dto.request.ApplyRequest;
import inha.dayoook_e.application.api.controller.dto.response.ApplicationResponse;
import inha.dayoook_e.application.api.mapper.ApplicationMapper;
import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.mapping.domain.Day;
import inha.dayoook_e.mapping.domain.TimeSlot;
import inha.dayoook_e.mapping.domain.repository.DayJpaRepository;
import inha.dayoook_e.mapping.domain.repository.TimeSlotJpaRepository;
import inha.dayoook_e.song.domain.TuteeSongProgress;
import inha.dayoook_e.tutor.domain.TutorSchedule;
import inha.dayoook_e.application.domain.repository.ApplicationJpaRepository;
import inha.dayoook_e.tutor.domain.id.TutorScheduleId;
import inha.dayoook_e.tutor.domain.repository.TutorScheduleJpaRepository;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.enums.Role;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static inha.dayoook_e.common.code.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService{

    private final ApplicationJpaRepository applicationJpaRepository;
    private final TutorScheduleJpaRepository tutorScheduleJpaRepository;
    private final  DayJpaRepository dayJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ApplicationMapper applicationMapper;


    public ApplicationResponse apply(User tutee, ApplyRequest applyRequest) {
        // 1. 튜터 조회
        User tutor = userJpaRepository.findById(applyRequest.tutorId()).orElseThrow(() -> new BaseException(NOT_FIND_USER));

        // 2. 튜터 권한 조회
        if (!tutor.getRole().equals(Role.TUTOR))
            throw new BaseException(INVALID_ROLE);

        // 3. 요일 조회
        Day day = dayJpaRepository.findById(applyRequest.dayId())
                .orElseThrow(() -> new BaseException(NOT_FIND_DAY));

        // 4. 시간대 조회
        TimeSlot timeSlot = timeSlotJpaRepository.findById(applyRequest.timeSlotId())
                .orElseThrow(() -> new BaseException(NOF_FIND_TIMESLOT));

        // 5. 튜터 스케줄 조회, 없으면 생성
        TutorScheduleId scheduleId = new TutorScheduleId(applyRequest.tutorId(), applyRequest.dayId(), applyRequest.timeSlotId());
        TutorSchedule tutorSchedule = tutorScheduleJpaRepository.findById(scheduleId)
                .orElseGet(() -> {
                    TutorSchedule newTutorSchedule = applicationMapper.toTutorSchedule(tutor, day, timeSlot, scheduleId);
                    return tutorScheduleJpaRepository.save(newTutorSchedule);
                });


        // 6-1. 스케쥴이 available 하다면 신청 생성
        if (tutorSchedule.getIsAvailable()) {
            tutorSchedule.makeUnavailable();
            Application application = applicationMapper.applyRequestToApplication(applyRequest, tutee, tutor, Status.APPLYING, LocalDateTime.now(), day, timeSlot);
            Application savedApplication = applicationJpaRepository.save(application);
            return applicationMapper.applicationToApplicationResponse(savedApplication);
        }
        // 6-2. 스케쥴이 unavailable 하다면 오류 반환
        else {
            throw new BaseException(SCHEDULE_ALREADY_BOOKED);
        }

    }

}
