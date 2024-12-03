package inha.dayoook_e.lesson.api.service;

import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.application.domain.repository.ApplicationGroupJpaRepository;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.lesson.api.controller.dto.request.CreateLessonRequest;
import inha.dayoook_e.lesson.api.controller.dto.response.LessonResponse;
import inha.dayoook_e.lesson.api.mapper.LessonMapper;
import inha.dayoook_e.lesson.domain.Lesson;
import inha.dayoook_e.lesson.domain.repository.*;
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

    private final LessonMapper lessonMapper;
    private final ApplicationGroupJpaRepository applicationGroupJpaRepository;
    private final LessonJpaRepository lessonJpaRepository;

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
}
