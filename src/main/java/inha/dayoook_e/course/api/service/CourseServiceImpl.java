package inha.dayoook_e.course.api.service;

import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.course.api.controller.dto.request.CreateCourseRequest;
import inha.dayoook_e.course.api.controller.dto.response.CourseResponse;
import inha.dayoook_e.course.api.mapper.CourseMapper;
import inha.dayoook_e.course.domain.Course;
import inha.dayoook_e.course.domain.repository.CourseJpaRepository;
import inha.dayoook_e.mapping.domain.Day;
import inha.dayoook_e.mapping.domain.TimeSlot;
import inha.dayoook_e.mapping.domain.repository.DayJpaRepository;
import inha.dayoook_e.mapping.domain.repository.TimeSlotJpaRepository;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.common.code.status.ErrorStatus.*;

/**
 * CourseServiceImpl은 강의 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseServiceImpl implements CourseService{

    private final CourseMapper courseMapper;
    private final CourseJpaRepository courseJpaReposiotry;
    private final UserJpaRepository userJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final DayJpaRepository dayJpaRepository;

    @Override
    public CourseResponse createCourse(CreateCourseRequest createCourseRequest) {
        // 1. 튜터,튜티,요일,시간대 조회
        User tutor = userJpaRepository.findByIdAndState(createCourseRequest.tutorId(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        User tutee = userJpaRepository.findByIdAndState(createCourseRequest.tuteeId(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        Day day = dayJpaRepository.findById(createCourseRequest.dayId())
                .orElseThrow(() -> new BaseException(NOT_FIND_DAY));
        TimeSlot timeSlot = timeSlotJpaRepository.findById(createCourseRequest.timeSlotId())
                .orElseThrow(() -> new BaseException(NOT_FIND_TIMESLOT));


        // 2. 강의 생성
        // Mapper 동작 오류로 인해 직접 생성
        Course course = Course.builder()
                .tutor(tutor)
                .tutee(tutee)
                .day(day)
                .timeSlot(timeSlot)
                .createdAt(LocalDateTime.now())
                .build();

        // 3. 강의 등록
        Course savedCourse = courseJpaReposiotry.save(course);
        return courseMapper.toCourseResponse(savedCourse);
    }
}
