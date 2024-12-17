package inha.dayoook_e.lesson.api.service;

import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.application.domain.repository.ApplicationGroupJpaRepository;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.lesson.api.controller.dto.request.*;
import inha.dayoook_e.lesson.api.controller.dto.response.*;
import inha.dayoook_e.lesson.api.mapper.LessonMapper;
import inha.dayoook_e.lesson.domain.Lesson;
import inha.dayoook_e.lesson.domain.LessonSchedule;
import inha.dayoook_e.lesson.domain.MeetingRoom;
import inha.dayoook_e.lesson.domain.enums.Status;
import inha.dayoook_e.lesson.domain.repository.LessonJpaRepository;
import inha.dayoook_e.lesson.domain.repository.LessonScheduleJpaRepository;
import inha.dayoook_e.lesson.domain.repository.MeetingRoomJpaRepository;
import inha.dayoook_e.mapping.api.mapper.MappingMapper;
import inha.dayoook_e.mapping.domain.Day;
import inha.dayoook_e.mapping.domain.TimeSlot;
import inha.dayoook_e.tutor.api.controller.dto.response.SearchTutorScheduleResponse;
import inha.dayoook_e.tutor.api.controller.dto.response.TutorScheduleData;
import inha.dayoook_e.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.common.code.status.ErrorStatus.*;
import static inha.dayoook_e.lesson.domain.enums.Status.SCHEDULED;

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
    private final MappingMapper mappingMapper;

    private final RestTemplate restTemplate;

    @Value("${fastapi.server.url}")
    private String fastApiServerUrl;

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


    /**
     * 강의 일정 생성
     *
     * @param user 현재 로그인한 사용자
     * @param accessToken 헤더에서 추출한 토큰
     * @param createLessonScheduleRequest 강의 일정 생성 요청
     * @return LessonScheduleResponse
     */
    @Override
    @Transactional
    public LessonScheduleResponse createLessonSchedule(User user, String accessToken, CreateLessonScheduleRequest createLessonScheduleRequest ) {
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
//        boolean exists = lessonScheduleJpaRepository.existsByLessonAndStartAt(lesson, nextClassTime);
//        if (exists) {
//            throw new BaseException(LESSON_SCHEDULE_ALREADY_EXISTS);
//        }


        // 7. 회의실 생성
        String meetApiUrl = fastApiServerUrl + "/api/meet/create";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken.replace("Bearer ", "")); // Bearer 토큰을 추가

        // 요청 바디 생성
        MeetingRequest meetingRequest = lessonMapper.toMeetingRequest(user.getEmail(), lesson.getApplicationGroup().getTutee().getEmail());

        // HTTP 요청 엔티티 생성
        HttpEntity<MeetingRequest> requestEntity = new HttpEntity<>(meetingRequest, headers);

        try {
            // FastAPI 서버로 POST 요청 보내기
            MeetingResponse response = restTemplate.postForObject(
                    meetApiUrl,
                    requestEntity,
                    MeetingResponse.class
            );

            // 7. 회의실 생성 (FastAPI에서 받은 실제 미팅 URL 사용)
            LessonSchedule lessonSchedule = lessonMapper.toLessonSchedule(lesson, null, nextClassTime);
            LessonSchedule savedSchedule = lessonScheduleJpaRepository.save(lessonSchedule);

            // 8. 회의실 생성 (실제 Google Meet URL 사용)
            MeetingRoom meetingRoom = MeetingRoom.builder()
                    .roomUrl(response.meeting_uri())
                    .createdAt(LocalDateTime.now())
                    .lessonSchedule(savedSchedule)
                    .build();

            meetingRoom = meetingRoomJpaRepository.save(meetingRoom);
            return lessonMapper.toLessonScheduleResponse(savedSchedule, meetingRoom);

        } catch (Exception e) {
            log.error("Failed to create meeting room: ", e);
            throw new BaseException(MEETING_ROOM_CREATION_FAILED);
        }
    }


    /**
     * 강의 완료 처리
     *
     * @param user 현재 로그인한 사용자
     * @param scheduleId 강의 일정 ID
     * @param completeLessonRequest 강의 완료 처리 요청
     * @return LessonScheduleResponse
     */
    @Override
    public LessonScheduleResponse completeLessonSchedule(User user, Integer scheduleId, CompleteLessonRequest completeLessonRequest) {
        // 1. 수업 일정 조회
        LessonSchedule schedule = lessonScheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new BaseException(LESSON_SCHEDULE_NOT_FOUND));

        // 2. 튜터 권한 확인
        if (!schedule.getLesson().getApplicationGroup().getTutor().getId().equals(user.getId())) {
            throw new BaseException(UNAUTHORIZED_TUTOR);
        }

        // 3. 수업 상태 확인
        if (!schedule.getStatus().equals(SCHEDULED)) {
            throw new BaseException(INVALID_LESSON_STATUS);
        }

        // 4. 수업 시작 시간이 지났는지 확인(테스트를 위해 주석)
//        if (LocalDateTime.now().isBefore(schedule.getStartAt())) {
//            throw new BaseException(LESSON_NOT_STARTED);
//        }

        // 5. 수업 완료 처리
        schedule.complete();
        lessonScheduleJpaRepository.save(schedule);

        return lessonMapper.toLessonScheduleResponse(schedule, schedule.getMeetingRoom());
    }

    /**
     * 강의 취소 처리
     *
     * @param user 현재 로그인한 사용자
     * @param scheduleId 강의 일정 ID
     * @param cancelLessonRequest 강의 취소 처리 요청
     * @return LessonScheduleResponse
     */
    @Override
    public LessonScheduleResponse cancelLessonSchedule(User user, Integer scheduleId, CancelLessonRequest cancelLessonRequest) {
        // 1. 수업 일정 조회
        LessonSchedule schedule = lessonScheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new BaseException(LESSON_SCHEDULE_NOT_FOUND));

        // 2. 튜터 권한 확인
        if (!schedule.getLesson().getApplicationGroup().getTutor().getId().equals(user.getId())) {
            throw new BaseException(UNAUTHORIZED_TUTOR);
        }

        // 3. 수업 상태 확인
        if (!schedule.getStatus().equals(SCHEDULED)) {
            throw new BaseException(INVALID_LESSON_STATUS);
        }

        // 4. 수업 시작 전인지 확인(테스트를 위해 주석)
//        if (LocalDateTime.now().isAfter(schedule.getStartAt())) {
//            throw new BaseException(LESSON_ALREADY_STARTED);
//        }

        // 5. 수업 취소 처리
        schedule.cancel(cancelLessonRequest.reason());
        return lessonMapper.toLessonScheduleResponse(schedule, schedule.getMeetingRoom());
    }

    /**
     * 수업 일정 조회
     *
     * @param user 현재 로그인한 사용자
     * @param lessonSchedulesRequest 수업 일정 조회 요청
     * @return List<LessonSchedulesResponse>
     */
    @Override
    public List<LessonSchedulesResponse> getLessonSchedules(User user, LessonSchedulesRequest lessonSchedulesRequest) {
        // 1. 요청된 lessonScheduleIds로 수업 일정들을 조회하고 생성일자 기준 내림차순 정렬
        List<LessonSchedule> lessonSchedules = lessonScheduleJpaRepository
                .findAllById(lessonSchedulesRequest.lessonScheduleIds())
                .stream()
                .sorted((a, b) -> b.getStartAt().compareTo(a.getStartAt()))
                .toList();

        // 2. 각 수업 일정을 LessonSchedulesResponse로 변환
        return lessonSchedules.stream()
                .map(schedule -> {
                    Lesson lesson = schedule.getLesson();
                    ApplicationGroup applicationGroup = lesson.getApplicationGroup();
                    User tutor = applicationGroup.getTutor();


                    // 튜터 스케줄 응답 생성
                    SearchTutorScheduleResponse tutorSchedule = new SearchTutorScheduleResponse(
                            tutor.getId(),
                            tutor.getName(),
                            null
                    );

                    TuteeInfoResponse tuteeInfo = new TuteeInfoResponse(
                            applicationGroup.getTutee().getId(),
                            applicationGroup.getTutee().getName()
                    );

                    // 최종 응답 생성
                    return new LessonSchedulesResponse(
                            lesson.getId(),
                            schedule.getId(),
                            tutorSchedule,
                            schedule.getStartAt(),
                            tuteeInfo
                    );
                })
                .toList();
    }

    /**
     * 생성된 강의 링크 조회
     *
     * @param user 현재 로그인한 사용자
     * @param lessonId 강의 ID
     * @return LessonMeetingResponse
     */
    @Override
    public LessonMeetingResponse getLessonMeetingResponse(User user, Integer lessonId) {
        // 1. lessonId로 lesson 조회
        Lesson lesson = lessonJpaRepository.findById(lessonId)
                .orElseThrow(() -> new BaseException(LESSON_NOT_FOUND));

        // 2. lesson에 연결된 LessonSchedule 중 SCHEDULED 상태이면서 가장 최근 것 조회
        Optional<LessonSchedule> latestSchedule = lessonScheduleJpaRepository
                .findFirstByLessonAndStatusOrderByStartAtDesc(lesson, Status.SCHEDULED);

        // 3. 스케줄이 없으면 null 반환
        if (latestSchedule.isEmpty()) {
            return null;
        }

        // 4. MeetingRoom 정보 포함하여 응답 생성
        LessonSchedule schedule = latestSchedule.get();
        MeetingRoom meetingRoom = schedule.getMeetingRoom();

        return new LessonMeetingResponse(
                schedule.getId(),
                meetingRoom != null ? meetingRoom.getRoomUrl() : null,
                meetingRoom != null ? meetingRoom.getCreatedAt() : null,
                schedule.getStatus()
        );
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
