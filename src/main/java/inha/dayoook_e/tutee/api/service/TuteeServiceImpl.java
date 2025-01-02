package inha.dayoook_e.tutee.api.service;

import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.application.domain.repository.ApplicationGroupJpaRepository;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.lesson.domain.LessonSchedule;
import inha.dayoook_e.lesson.domain.repository.LessonScheduleJpaRepository;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;
import inha.dayoook_e.mapping.api.mapper.MappingMapper;
import inha.dayoook_e.tutee.api.controller.dto.response.DaySchedule;
import inha.dayoook_e.tutee.api.controller.dto.response.LessonInfo;
import inha.dayoook_e.tutee.api.controller.dto.response.SearchTuteeApplicationResponse;
import inha.dayoook_e.tutee.api.controller.dto.response.TuteeScheduleResponse;
import inha.dayoook_e.tutee.api.mapper.TuteeMapper;
import inha.dayoook_e.tutor.api.controller.dto.request.ScheduleTimeSlot;
import inha.dayoook_e.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.common.Constant.CREATE_AT;
import static inha.dayoook_e.common.code.status.ErrorStatus.INVALID_DAY_NAME;

/**
 * TuteeServiceImpl은 튜티 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TuteeServiceImpl implements TuteeService {


    private final TuteeMapper tuteeMapper;
    private final ApplicationGroupJpaRepository applicationGroupJpaRepository;
    private final MappingMapper mappingMapper;
    private final LessonScheduleJpaRepository lessonScheduleJpaRepository;

    @Override
    public Slice<SearchTuteeApplicationResponse> getTuteeApplications(User user, Integer page, Status status) {
        PageRequest pageRequest = PageRequest.of(page, 10 + 1, Sort.by(Sort.Direction.DESC, CREATE_AT));

        Slice<ApplicationGroup> applications;
        if (status != null) {
            applications = applicationGroupJpaRepository
                    .findSliceByTuteeAndStatusAndState(user, status, ACTIVE, pageRequest);
        } else {
            applications = applicationGroupJpaRepository
                    .findSliceByTuteeAndState(user, ACTIVE, pageRequest);
        }

        return applications.map(applicationGroup -> {
            List<SearchLanguagesResponse> languages = applicationGroup.getTutor().getUserLanguages().stream()
                    .map(userLanguage -> mappingMapper.toSearchLanguagesResponse(
                            userLanguage.getLanguage().getId(),
                            userLanguage.getLanguage().getName()
                    ))
                    .toList();

            List<ScheduleTimeSlot> scheduleTimeSlots = applicationGroup.getApplications().stream()
                    .map(application -> mappingMapper.toScheduleTimeSlot(
                            application.getDay().getId(),
                            application.getTimeSlot().getId()
                    ))
                    .toList();

            return tuteeMapper.toSearchTuteeApplicationResponse(applicationGroup, languages, scheduleTimeSlots);
        });
    }

    /**
     * 튜티의 월별 스케줄을 조회합니다.
     *
     * @param user  튜티 정보
     * @param year  년도
     * @param month 월
     * @return 튜티의 월별 스케줄 조회 결과
     */
    @Override
    public TuteeScheduleResponse getTuteeSchedule(User user, Integer year, Integer month) {
        LocalDate now = LocalDate.now();
        year = year != null ? year : now.getYear();
        month = month != null ? month : now.getMonthValue();

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // 1. 수락된 신청 정보와 레슨 정보 함께 조회
        List<ApplicationGroup> acceptedApplications = applicationGroupJpaRepository
                .findAllByTuteeAndStatusAndState(user, Status.APPROVED, ACTIVE);

        Map<Integer, LocalDateTime> lessonCreationTimes = new HashMap<>();
        for (ApplicationGroup applicationGroup : acceptedApplications) {
            applicationGroup.getLesson()
                    .ifPresent(lesson -> {
                        lessonCreationTimes.put(applicationGroup.getId(), lesson.getCreatedAt());
                    });
        }

        // 2. 생성된 수업 일정 조회
        List<LessonSchedule> monthlySchedules = lessonScheduleJpaRepository
                .findAllByLesson_ApplicationGroup_TuteeAndStartAtBetween(
                        user,
                        startDate.atStartOfDay(),
                        endDate.atTime(23, 59, 59)
                );

        // 3. 날짜별로 수업 정보 그룹화
        Map<Integer, List<LessonSchedule>> schedulesByDay = monthlySchedules.stream()
                .collect(Collectors.groupingBy(
                        schedule -> schedule.getStartAt().getDayOfMonth()
                ));

        // 4. 일별 스케줄 생성
        List<DaySchedule> monthlyCalendar = new ArrayList<>();

        for (int day = 1; day <= endDate.getDayOfMonth(); day++) {
            List<LessonSchedule> dailySchedules = schedulesByDay.getOrDefault(day, Collections.emptyList());
            List<LessonInfo> lessonsForDay = new ArrayList<>();

            // 4-1. 생성된 수업 일정 처리
            lessonsForDay.addAll(dailySchedules.stream()
                    .map(schedule -> new LessonInfo(
                            schedule.getLesson().getId(),
                            schedule.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")),
                            schedule.getLesson().getApplicationGroup().getTutor().getId(),
                            schedule.getLesson().getApplicationGroup().getTutor().getName(),
                            schedule.getStatus()
                    ))
                    .toList());

            // 4-2. 신청된 수업 중 아직 생성되지 않은 일정 처리
            LocalDate currentDate = startDate.withDayOfMonth(day);
            DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();

            for (ApplicationGroup applicationGroup : acceptedApplications) {
                // 레슨이 생성된 경우, 생성 시점 이후의 일정만 처리
                LocalDateTime lessonCreationTime = lessonCreationTimes.get(applicationGroup.getId());
                if (lessonCreationTime != null && currentDate.atStartOfDay().isBefore(lessonCreationTime)) {
                    continue;
                }

                applicationGroup.getApplications().stream()
                        .filter(application -> {
                            DayOfWeek applicationDay = convertToDayOfWeek(application.getDay().getName());
                            return applicationDay == currentDayOfWeek &&
                                    dailySchedules.stream().noneMatch(schedule ->
                                            schedule.getLesson().getApplicationGroup().getId().equals(applicationGroup.getId()));
                        })
                        .forEach(application -> {
                            lessonsForDay.add(new LessonInfo(
                                    null,
                                    application.getTimeSlot().getTime(),
                                    applicationGroup.getTutor().getId(),
                                    applicationGroup.getTutor().getName(),
                                    inha.dayoook_e.lesson.domain.enums.Status.NOT_CREATED
                            ));
                        });
            }

            // 시간순 정렬
            List<LessonInfo> sortedLessons = lessonsForDay.stream()
                    .sorted(Comparator.comparing(LessonInfo::startTime))
                    .toList();

            if (!sortedLessons.isEmpty()) {
                monthlyCalendar.add(new DaySchedule(day, sortedLessons));
            }
        }

        return new TuteeScheduleResponse(year, month, monthlyCalendar);
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

}
