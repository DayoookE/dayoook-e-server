package inha.dayoook_e.tutor.api.service;

import inha.dayoook_e.application.domain.Application;
import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.application.domain.repository.ApplicationGroupJpaRepository;
import inha.dayoook_e.application.domain.repository.ApplicationJpaRepository;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchAgeGroupResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchDayResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchTimeSlotResponse;
import inha.dayoook_e.mapping.api.mapper.MappingMapper;
import inha.dayoook_e.mapping.domain.Day;
import inha.dayoook_e.mapping.domain.TimeSlot;
import inha.dayoook_e.mapping.domain.repository.DayJpaRepository;
import inha.dayoook_e.mapping.domain.repository.TimeSlotJpaRepository;
import inha.dayoook_e.tutor.api.controller.dto.request.ScheduleTimeSlot;
import inha.dayoook_e.tutor.api.controller.dto.request.SearchCond;
import inha.dayoook_e.tutor.api.controller.dto.request.TutorScheduleRequest;
import inha.dayoook_e.tutor.api.controller.dto.response.*;
import inha.dayoook_e.tutor.api.mapper.TutorMapper;
import inha.dayoook_e.tutor.domain.Experience;
import inha.dayoook_e.tutor.domain.TutorAgeGroup;
import inha.dayoook_e.tutor.domain.TutorInfo;
import inha.dayoook_e.tutor.domain.TutorSchedule;
import inha.dayoook_e.tutor.domain.id.TutorScheduleId;
import inha.dayoook_e.tutor.domain.repository.ExperienceJpaRepository;
import inha.dayoook_e.tutor.domain.repository.TutorAgeGroupJpaRepository;
import inha.dayoook_e.tutor.domain.repository.TutorQueryRepository;
import inha.dayoook_e.tutor.domain.repository.TutorScheduleJpaRepository;
import inha.dayoook_e.user.api.controller.dto.response.TuteeInfoResponse;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.UserLanguage;
import inha.dayoook_e.user.domain.enums.Role;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import inha.dayoook_e.user.domain.repository.UserLanguageJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.common.Constant.CREATE_AT;
import static inha.dayoook_e.common.Constant.NAME;
import static inha.dayoook_e.common.code.status.ErrorStatus.*;
import static inha.dayoook_e.user.domain.enums.Role.TUTOR;

/**
 * TutorServiceImpl은 튜터 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TutorServiceImpl implements TutorService {


    private final ExperienceJpaRepository experienceJpaRepository;
    private final TutorAgeGroupJpaRepository tutorAgeGroupJpaRepository;
    private final UserLanguageJpaRepository userLanguageJpaRepository;
    private final TutorQueryRepository tutorQueryRepository;
    private final ApplicationJpaRepository applicationJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ApplicationGroupJpaRepository applicationGroupJpaRepository;
    private final TutorMapper tutorMapper;
    private final TutorScheduleJpaRepository tutorScheduleJpaRepository;
    private final DayJpaRepository dayJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final MappingMapper mappingMapper;

    /**
     * 튜터 목록 조건 조회
     *
     * @param searchCond 검색 조건
     * @param page 페이지 번호
     * @return 튜터 목록 조회 결과
     */
    @Override
    public Slice<TutorSearchPageResponse> getTutors(SearchCond searchCond, Integer page) {
        // 결과는 이름 순으로 정렬
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC,NAME));

        return tutorQueryRepository.searchTutors(searchCond, pageable);
    }

    /**
     * 튜터 조회
     * @param tutorId 조회 할 튜터 ID
     * @return TutorSearchResponse
     */
    @Override
    public TutorSearchResponse getTutor(Integer tutorId) {
        // 1. tutorId로 튜터 조회
        User tutor = userJpaRepository.findByIdAndState(tutorId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        // 2. Role이 tutor인지 확인
        if (tutor.getRole().equals(TUTOR) == false)
            throw new BaseException(INVALID_ROLE);

        TutorInfo tutorInfo = tutor.getTutorInfo();

        // 2-1. 조회된 tutor의 Id로 UserLanguages 조회
        List<UserLanguage> languageList = userLanguageJpaRepository.findByUserId(tutorId);
        // 2-2. 조회 된 UserLanguageList를 SearchLanguageResponseList로 변환
        List<SearchLanguagesResponse> searchLanguagesResponses = languageList.stream().map(
                userLanguage -> mappingMapper.userLanguageToSearchLanguageResponse(userLanguage)
        ).toList();

        // 3-1. 조회된 tutor의 Id로 TutorAgeGroup 조회
        List<TutorAgeGroup> ageGroupList = tutorAgeGroupJpaRepository.findByUserId(tutorId);
        // 3-2. 조회된 TutorAgeGroupList를 SearchAgeGroupResponseList로 변환
        List<SearchAgeGroupResponse> searchAgeGroupResponses = ageGroupList.stream().map(
                tutorAgeGroup -> mappingMapper.toSearchAgeGroupResponse(tutorAgeGroup)
        ).toList();

        // 4-1. 조회된 tutor의 Id로 Experience 조회
        List<Experience> experienceList = experienceJpaRepository.findByUserId(tutorId);
        // 4-2. 조회된 ExperienceList 를 SearchExperienceResponseList로 변환
        List<SearchExperienceResponse> searchExperienceResponses = experienceList.stream().map(
                experience -> tutorMapper.toSearchExperienceResponse(experience)
        ).toList();

        // 5-1. 조회된 tutor의 Id로 사용 가능한 스케줄 조회
        List<TutorSchedule> scheduleList = tutorScheduleJpaRepository.findByUserIdAndIsAvailable(tutorId, true);
        // 5-2. 조회된 스케줄을 ScheduleTimeSlot 리스트로 변환
        List<ScheduleTimeSlot> scheduleTimeSlots = scheduleList.stream()
                .map(schedule -> mappingMapper.toScheduleTimeSlot(schedule.getDay().getId(), schedule.getTimeSlot().getId()
                ))
                .toList();

        return tutorMapper.toTutorSearchResponse(
                tutor,
                tutorInfo,
                searchLanguagesResponses,
                searchAgeGroupResponses,
                searchExperienceResponses,
                scheduleTimeSlots
        );
    }

    /**
     * 튜터 일정 생성
     *
     * @param user 사용자 정보
     * @param tutorScheduleRequest 튜터 일정 생성 요청
     * @return TutorResponse
     */
    @Override
    public TutorResponse createSchedule(User user, TutorScheduleRequest tutorScheduleRequest) {
        // 1. Role이 TUTOR인지 확인
        if (!user.getRole().equals(TUTOR)) {
            throw new BaseException(INVALID_ROLE);
        }

        // 2. 요청된 스케줄의 요일ID와 시간대ID Set 생성
        List<ScheduleTimeSlot> requestedSlots = tutorScheduleRequest.scheduleTimeSlots();
        Set<Integer> requestedDayIds = requestedSlots.stream()
                .map(ScheduleTimeSlot::dayId)
                .collect(Collectors.toSet());
        Set<Integer> requestedTimeSlotIds = requestedSlots.stream()
                .map(ScheduleTimeSlot::timeSlotId)
                .collect(Collectors.toSet());

        // 3. 요일과 시간대 정보 조회
        Map<Integer, Day> dayMap = dayJpaRepository.findAllById(requestedDayIds).stream()
                .collect(Collectors.toMap(Day::getId, day -> day));
        Map<Integer, TimeSlot> timeSlotMap = timeSlotJpaRepository.findAllById(requestedTimeSlotIds).stream()
                .collect(Collectors.toMap(TimeSlot::getId, timeSlot -> timeSlot));

        // 유효성 검증
        validateRequestedIds(requestedDayIds, dayMap.keySet(), requestedTimeSlotIds, timeSlotMap.keySet());

        // 4. 현재 존재하는 모든 스케줄 조회 (available true/false 모두)
        List<TutorSchedule> allExistingSchedules = tutorScheduleJpaRepository.findByUserId(user.getId());

        // 5. 요청된 스케줄 ID들의 Set 생성
        Set<TutorScheduleId> requestedScheduleIds = requestedSlots.stream()
                .map(slot -> tutorMapper.toTutorScheduleId(user.getId(), slot.dayId(), slot.timeSlotId()))
                .collect(Collectors.toSet());

        // 6. 기존 스케줄 처리
        List<TutorSchedule> schedulesToUpdate = new ArrayList<>();

        // 6-1. 기존 스케줄 순회하면서 처리
        for (TutorSchedule existingSchedule : allExistingSchedules) {
            boolean isRequestedNow = requestedScheduleIds.contains(existingSchedule.getId());

            // 중요: 승인된 강의 시간대는 기본적으로 unavailable로 유지
            List<Application> approvedApplications = applicationJpaRepository.findAll().stream()
                    .filter(app ->
                            app.getTutor().getId().equals(user.getId()) &&
                                    app.getDay().getId().equals(existingSchedule.getDay().getId()) &&
                                    app.getTimeSlot().getId().equals(existingSchedule.getTimeSlot().getId()) &&
                                    app.getApplicationGroup().getStatus().equals(Status.APPROVED)
                    )
                    .collect(Collectors.toList());

            boolean hasApprovedApplications = !approvedApplications.isEmpty();

            // 로직 조건 변경
            if (hasApprovedApplications) {
                // 승인된 강의가 있는 시간대는 무조건 unavailable
                if (existingSchedule.getIsAvailable()) {
                    existingSchedule.makeUnavailable();
                    schedulesToUpdate.add(existingSchedule);
                }
            } else {
                // 승인된 강의가 없는 경우에만 기존 로직 적용
                if (Boolean.TRUE.equals(existingSchedule.getIsAvailable()) && !isRequestedNow) {
                    existingSchedule.makeUnavailable();
                    schedulesToUpdate.add(existingSchedule);
                } else if (Boolean.TRUE.equals(!existingSchedule.getIsAvailable()) && isRequestedNow) {
                    existingSchedule.makeAvailable();
                    schedulesToUpdate.add(existingSchedule);
                }
            }

            // 처리된 ID는 요청 목록에서 제거
            requestedScheduleIds.remove(existingSchedule.getId());
        }

        // 7. 남은 요청된 ID에 대해 새로운 스케줄 생성 (모두 available = true로)
        List<TutorSchedule> newSchedules = requestedScheduleIds.stream()
                .map(scheduleId -> {
                    Day day = dayMap.get(scheduleId.getDayId());
                    TimeSlot timeSlot = timeSlotMap.get(scheduleId.getTimeSlotId());
                    return tutorMapper.toTutorSchedule(user, day, timeSlot);
                })
                .collect(Collectors.toList());

        // 8. 모든 변경사항 저장
        if (!schedulesToUpdate.isEmpty()) {
            tutorScheduleJpaRepository.saveAll(schedulesToUpdate);
        }
        if (!newSchedules.isEmpty()) {
            tutorScheduleJpaRepository.saveAll(newSchedules);
        }

        // 9. 응답 생성
        return tutorMapper.toTutorResponse(user);
    }



    /**
     * 튜터 일정 조회
     *
     * @param user 사용자 정보
     * @param tutorId 튜터 ID
     * @return SearchTutorScheduleResponse
     */
    @Override
    public SearchTutorScheduleResponse getTutorSchedule(User user, Integer tutorId) {
        // 1. tutorId로 튜터 조회
        User tutor = userJpaRepository.findByIdAndState(tutorId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        // 2. Role이 TUTOR인지 확인
        if (!tutor.getRole().equals(TUTOR) && !user.getId().equals(tutorId) && !user.getRole().equals(Role.ADMIN)) {
            throw new BaseException(INVALID_ROLE);
        }

        // 3. 모든 요일과 시간대 조회
        List<Day> allDays = dayJpaRepository.findAll();
        List<TimeSlot> allTimeSlots = timeSlotJpaRepository.findAll();

        // 4. 튜터의 기존 스케줄 조회
        List<TutorSchedule> existingSchedules = tutorScheduleJpaRepository.findByUserId(tutorId);

        // 5. 모든 가능한 조합에 대한 스케줄 데이터 생성
        List<TutorScheduleData> scheduleDataList = new ArrayList<>();

        for (Day day : allDays) {
            for (TimeSlot timeSlot : allTimeSlots) {
                // 해당 요일과 시간대에 대한 기존 스케줄 찾기
                boolean isAvailable = existingSchedules.stream()
                        .filter(schedule ->
                                schedule.getDay().getId().equals(day.getId()) &&
                                        schedule.getTimeSlot().getId().equals(timeSlot.getId())
                        )
                        .findFirst()
                        .map(TutorSchedule::getIsAvailable)
                        .orElse(false); // 스케줄이 없는 경우 false 반환

                // 응답 데이터 생성
                SearchDayResponse dayResponse = mappingMapper.toSearchDayResponse(day.getId(), day.getName());
                SearchTimeSlotResponse timeSlotResponse = mappingMapper.toSearchTimeSlotResponse(timeSlot.getId(), timeSlot.getTime());
                TutorScheduleData scheduleData = tutorMapper.toTutorScheduleData(dayResponse, timeSlotResponse, isAvailable);
                scheduleDataList.add(scheduleData);
            }
        }
        // 6. 최종 응답 생성
        return tutorMapper.toSearchTutorScheduleResponse(tutor, scheduleDataList);
    }

    @Override
    public Page<SearchTutorApplicationResponse> getTutorApplication(User user, Integer tutorId, Integer page, Status status) {
        // 1. 페이징 설정 (최근 신청 순으로 정렬)
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, CREATE_AT));

        // 2. 튜터 존재 여부 확인
        User tutor = userJpaRepository.findByIdAndState(tutorId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        // 3. 권한 검증 (튜터 자신 또는 관리자만 조회 가능)
        if (!user.getId().equals(tutorId) && !user.getRole().equals(Role.ADMIN)) {
            throw new BaseException(INVALID_ROLE);
        }

        // 4. 신청 그룹 조회 (특정 상태의 신청 그룹만 조회)
        Page<ApplicationGroup> applicationGroups;
        if (status == null) {
            applicationGroups = applicationGroupJpaRepository.findByTutor(tutor, pageable);
        } else {
            applicationGroups = applicationGroupJpaRepository.findByTutorAndStatus(tutor, status, pageable);
        }

        // 5. DTO 변환
        return applicationGroups.map(applicationGroup -> {
            // 튜티 기본 정보 변환
            TuteeInfoResponse tuteeInfo = TuteeInfoResponse.builder()
                    .id(applicationGroup.getTutee().getId())
                    .role(applicationGroup.getTutee().getRole())
                    .name(applicationGroup.getTutee().getName())
                    .profileUrl(applicationGroup.getTutee().getProfileUrl())
                    .gender(applicationGroup.getTutee().getGender())
                    .age(applicationGroup.getTutee().getAge())
                    .point(applicationGroup.getTutee().getTuteeInfo().getPoint())
                    .level(applicationGroup.getTutee().getTuteeInfo().getLevel())
                    .build();

            // 튜티 언어 정보 변환
            List<SearchLanguagesResponse> languages = applicationGroup.getTutee().getUserLanguages().stream()
                    .map(userLanguage -> mappingMapper.toSearchLanguagesResponse(userLanguage.getLanguage().getId(), userLanguage.getLanguage().getName()))
                    .toList();

            // 신청 시간대 정보 변환
            List<ScheduleTimeSlot> scheduleTimeSlots = applicationGroup.getApplications().stream()
                    .map(application -> mappingMapper.toScheduleTimeSlot(application.getDay().getId(), application.getTimeSlot().getId()
                    ))
                    .toList();

            // SearchTutorApplicationResponse 생성
            return tutorMapper.toSearchTutorApplicationResponse(applicationGroup, tuteeInfo, languages, scheduleTimeSlots);
        });
    }

    private void validateRequestedIds(
            Set<Integer> requestedDayIds,
            Set<Integer> foundDayIds,
            Set<Integer> requestedTimeSlotIds,
            Set<Integer> foundTimeSlotIds
    ) {
        if (!requestedDayIds.equals(foundDayIds)) {
            throw new BaseException(INVALID_DAY_ID);
        }
        if (!requestedTimeSlotIds.equals(foundTimeSlotIds)) {
            throw new BaseException(INVALID_TIME_SLOT_ID);
        }
    }


}
