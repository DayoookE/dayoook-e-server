package inha.dayoook_e.tutor.api.service;

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
import inha.dayoook_e.tutor.api.controller.dto.request.SearchCond;
import inha.dayoook_e.tutor.api.controller.dto.request.TutorScheduleRequest;
import inha.dayoook_e.tutor.api.controller.dto.response.*;
import inha.dayoook_e.tutor.api.mapper.TutorMapper;
import inha.dayoook_e.tutor.domain.Experience;
import inha.dayoook_e.tutor.domain.TutorAgeGroup;
import inha.dayoook_e.tutor.domain.TutorInfo;
import inha.dayoook_e.tutor.domain.TutorSchedule;
import inha.dayoook_e.tutor.domain.id.TutorScheduleId;
import inha.dayoook_e.tutor.domain.repository.*;
import inha.dayoook_e.user.api.mapper.UserMapper;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.UserLanguage;
import inha.dayoook_e.user.domain.enums.Role;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import inha.dayoook_e.user.domain.repository.UserLanguageJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.common.Constant.NAME;
import static inha.dayoook_e.common.code.status.ErrorStatus.INVALID_ROLE;
import static inha.dayoook_e.common.code.status.ErrorStatus.NOT_FIND_USER;
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
    private final UserJpaRepository userJpaRepository;
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

        // 3-2. 조회된 TutorAgeGroupList를  SearchAgeGroupResponseList로 변환
        List<SearchAgeGroupResponse> searchAgeGroupResponses = ageGroupList.stream().map(
                tutorAgeGroup -> mappingMapper.toSearchAgeGroupResponse(tutorAgeGroup)
        ).toList();


        // 4-1. 조회된 tutor의 Id로 Experience 조회
        List<Experience> experienceList = experienceJpaRepository.findByUserId(tutorId);

        // 4-2. 조회된 ExperienceList 를 SearchExperienceResponseList로 변환
        List<SearchExperienceResponse> searchExperienceResponses = experienceList.stream().map(
                experience -> tutorMapper.toSearchExperienceResponse(experience)
        ).toList();


        return tutorMapper.toTutorSearchResponse(tutor, tutorInfo, searchLanguagesResponses, searchAgeGroupResponses, searchExperienceResponses);
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

        // 2. 요일과 시간대 정보 조회
        List<Day> days = dayJpaRepository.findAllById(tutorScheduleRequest.dayIds());
        List<TimeSlot> timeSlots = timeSlotJpaRepository.findAllById(tutorScheduleRequest.timeSlotIds());

        // 3. 현재 존재하는 스케줄 조회
        List<TutorSchedule> existingSchedules = tutorScheduleJpaRepository.findByUserId(user.getId());

        // 4. 새로운 스케줄 조합 생성
        List<TutorSchedule> newSchedules = new ArrayList<>();

        for (Day day : days) {
            for (TimeSlot timeSlot : timeSlots) {
                TutorScheduleId scheduleId = tutorMapper.toTutorScheduleId(
                        user.getId(),
                        day.getId(),
                        timeSlot.getId()
                );

                // 기존 스케줄에 있는지 확인
                boolean exists = existingSchedules.stream()
                        .anyMatch(schedule -> schedule.getId().equals(scheduleId));
                if (!exists) {
                    // 새로운 스케줄 생성
                    TutorSchedule schedule = tutorMapper.toTutorSchedule(user, day, timeSlot);
                    newSchedules.add(schedule);
                }
            }
        }

        // 5. 기존 스케줄 중 새로운 요청에 없는 스케줄은 isAvailable을 false로 설정
        existingSchedules.forEach(schedule -> {
            boolean keepAvailable = days.stream()
                    .anyMatch(day -> day.getId().equals(schedule.getId().getDayId()))
                    &&
                    timeSlots.stream()
                            .anyMatch(timeSlot -> timeSlot.getId().equals(schedule.getId().getTimeSlotId()));

            if (!keepAvailable) {
                schedule.makeUnavailable();
            }
        });

        // 6. 새로운 스케줄 저장
        if (!newSchedules.isEmpty()) {
            tutorScheduleJpaRepository.saveAll(newSchedules);
        }

        // 7. 응답 생성
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
}
