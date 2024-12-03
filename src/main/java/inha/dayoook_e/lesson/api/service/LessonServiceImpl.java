package inha.dayoook_e.lesson.api.service;

import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.application.domain.repository.ApplicationGroupJpaRepository;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonRequest;
import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonScheduleRequest;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonResponse;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonScheduleResponse;
import inha.dayoook_e.lesson.api.mapper.LessonMapper;
import inha.dayoook_e.lesson.domain.Lesson;
import inha.dayoook_e.lesson.domain.LessonSchedule;
import inha.dayoook_e.lesson.domain.MeetingRoom;
import inha.dayoook_e.lesson.domain.repository.*;
import inha.dayoook_e.mapping.domain.Day;
import inha.dayoook_e.mapping.domain.TimeSlot;
import inha.dayoook_e.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.UUID;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.common.code.status.ErrorStatus.*;

/**
 * LessonServiceImpl은 교육 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LessonServiceImpl implements LessonService {

    private final LessonMapper lessonMapper;
    private final ApplicationGroupJpaRepository applicationGroupJpaRepository;
    private final LessonJpaRepository lessonJpaRepository;
    private final LessonScheduleJpaRepository lessonScheduleJpaRepository;
    private final MeetingRoomJpaRepository meetingRoomJpaRepository;

    /**
     * 강의 생성
     *
     * @param createLessonRequest 강의 생성 요청
     * @return LessonResponse
     */
    @Override
    public LessonResponse createLesson(CreateLessonRequest createLessonRequest) {

        ApplicationGroup applicationGroup = applicationGroupJpaRepository.findByIdAndState(createLessonRequest.id(), ACTIVE)
                .orElseThrow(() -> new BaseException(APPLICATION_GROUP_NOT_FOUND));


        Lesson lesson = lessonMapper.toLesson(applicationGroup);

        // 3. 강의 등록
        Lesson savedLesson = lessonJpaRepository.save(lesson);
        return lessonMapper.toLessonResponse(savedLesson);
    }


    @Override
    @Transactional
    public LessonScheduleResponse createLessonSchedule(User user, CreateLessonScheduleRequest createLessonScheduleRequest ) {
        // 1. 수업 존재 여부 확인 및 관련 정보 조회
        Lesson lesson = lessonJpaRepository.findByIdAndState(createLessonScheduleRequest.lessonId(), ACTIVE)
                .orElseThrow(() -> new BaseException(LESSON_NOT_FOUND));

        ApplicationGroup applicationGroup = lesson.getApplicationGroup();

        // 2. 튜터 권한 확인
        if (!applicationGroup.getTutor().getId().equals(user.getId())) {
            throw new BaseException(UNAUTHORIZED_TUTOR);
        }

        // 3. Application에서 요일과 시간 정보 가져오기
        Application firstApplication = applicationGroup.getApplications()
                .stream()
                .findFirst()
                .orElseThrow(() -> new BaseException(APPLICATION_NOT_FOUND));

        Day day = firstApplication.getDay();
        TimeSlot timeSlot = firstApplication.getTimeSlot();

        // 4. 현재 시간 기준으로 다음 수업 시작 시간 계산
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextClassTime = calculateNextClassTime(now, day.getName(), timeSlot.getTime());

        // 5. 수업 시작 10분 전 체크(테스트를 위해 주석)
//        if (now.plusMinutes(10).isBefore(nextClassTime)) {
//            throw new BaseException(TOO_EARLY_TO_CREATE_LESSON);
//        }

        // 6. 해당 시간에 이미 수업이 있는지 확인
        boolean exists = lessonScheduleJpaRepository.existsByLessonAndStartAt(lesson, nextClassTime);
        if (exists) {
            throw new BaseException(LESSON_SCHEDULE_ALREADY_EXISTS);
        }

        // 7. 회의실 생성 (더미 데이터)
        LessonSchedule lessonSchedule = lessonMapper.toLessonSchedule(lesson, null, nextClassTime);
        LessonSchedule savedSchedule = lessonScheduleJpaRepository.save(lessonSchedule);

        // 8. 회의실 생성
        String dummyRoomUrl = "https://meet.dayoook-e.com/" + UUID.randomUUID();
        MeetingRoom meetingRoom = MeetingRoom.builder()
                .roomUrl(dummyRoomUrl)
                .createdAt(now)
                .lessonSchedule(savedSchedule)  // 저장된 스케줄 참조
                .build();

        meetingRoom = meetingRoomJpaRepository.save(meetingRoom);
        return lessonMapper.toLessonScheduleResponse(savedSchedule, meetingRoom);
    }

    private LocalDateTime calculateNextClassTime(LocalDateTime now, String dayName, String timeSlot) {
        // 요일을 DayOfWeek으로 변환
        DayOfWeek targetDay = convertToDayOfWeek(dayName);

        // 시간 파싱 (예: "오후 1시" -> 13)
        int hour = parseTimeSlot(timeSlot);

        // 현재 날짜에서 시간만 변경
        LocalDateTime targetTime = now.withHour(hour).withMinute(0).withSecond(0).withNano(0);

        // 만약 현재 요일이 목표 요일보다 뒤라면 다음 주로 설정
        while (targetTime.getDayOfWeek() != targetDay) {
            targetTime = targetTime.plusDays(1);
        }

        return targetTime;
    }

    private DayOfWeek convertToDayOfWeek(String dayName) {
        return switch (dayName) {
            case "월요일" -> DayOfWeek.MONDAY;
            case "화요일" -> DayOfWeek.TUESDAY;
            case "수요일" -> DayOfWeek.WEDNESDAY;
            case "목요일" -> DayOfWeek.THURSDAY;
            case "금요일" -> DayOfWeek.FRIDAY;
            case "토요일" -> DayOfWeek.SATURDAY;
            case "일요일" -> DayOfWeek.SUNDAY;
            default -> throw new BaseException(INVALID_DAY_NAME);
        };
    }

    private int parseTimeSlot(String timeSlot) {
        // "오전 9시" or "오후 1시" 형식 파싱
        String[] parts = timeSlot.split(" ");
        int hour = Integer.parseInt(parts[1].replace("시", ""));

        if (parts[0].equals("오후") && hour != 12) {
            hour += 12;
        }
        if (parts[0].equals("오전") && hour == 12) {
            hour = 0;
        }

        return hour;
    }
}
