package inha.dayoook_e.lesson.api.service;

import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonRequest;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonResponse;
import inha.dayoook_e.lesson.api.mapper.LessonMapper;
import inha.dayoook_e.lesson.domain.Lesson;
import inha.dayoook_e.lesson.domain.repository.*;
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

    private final LessonRecordingJpaRepository lessonRecordingJpaRepository;
    private final LessonReviewJpaRepository lessonReviewJpaRepository;
    private final LessonScheduleJpaRepository lessonScheduleJpaRepository;
    private final MeetingRoomJpaRepository meetingRoomJpaRepository;
    private final LessonMapper lessonMapper;

    private final LessonJpaRepository lessonJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final DayJpaRepository dayJpaRepository;

    /**
     * 강의 생성
     *
     * @param createLessonRequest 강의 생성 요청
     * @return LessonResponse
     */
    @Override
    public LessonResponse createLesson(CreateLessonRequest createLessonRequest) {
        // 1. 튜터,튜티,요일,시간대 조회
        User tutor = userJpaRepository.findByIdAndState(createLessonRequest.tutorId(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        User tutee = userJpaRepository.findByIdAndState(createLessonRequest.tuteeId(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        Day day = dayJpaRepository.findById(createLessonRequest.dayId())
                .orElseThrow(() -> new BaseException(NOT_FIND_DAY));
        TimeSlot timeSlot = timeSlotJpaRepository.findById(createLessonRequest.timeSlotId())
                .orElseThrow(() -> new BaseException(NOT_FIND_TIMESLOT));


        // 2. 강의 생성
        // Mapper 동작 오류로 인해 직접 생성
        Lesson lesson = Lesson.builder()
                .tutor(tutor)
                .tutee(tutee)
                .day(day)
                .timeSlot(timeSlot)
                .build();

        // 3. 강의 등록
        Lesson savedLesson = lessonJpaRepository.save(lesson);
        return lessonMapper.toLessonResponse(savedLesson);
    }
}
