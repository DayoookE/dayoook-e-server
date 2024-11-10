package inha.dayoook_e.lesson.api.service;

import inha.dayoook_e.lesson.api.mapper.LessonMapper;
import inha.dayoook_e.lesson.domain.repository.LessonRecordingJpaRepository;
import inha.dayoook_e.lesson.domain.repository.LessonReviewJpaRepository;
import inha.dayoook_e.lesson.domain.repository.LessonScheduleJpaRepository;
import inha.dayoook_e.lesson.domain.repository.MeetingRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
