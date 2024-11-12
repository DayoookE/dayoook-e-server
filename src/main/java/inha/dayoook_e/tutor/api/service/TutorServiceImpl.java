package inha.dayoook_e.tutor.api.service;

import inha.dayoook_e.application.domain.repository.ApplicationJpaRepository;
import inha.dayoook_e.tutor.api.mapper.TutorMapper;
import inha.dayoook_e.tutor.domain.repository.*;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TutorServiceImpl은 튜터 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TutorServiceImpl implements TutorService {


    private final TutorInfoJpaRepository tutorInfoJpaRepository;
    private final ExperienceJpaRepository experienceJpaRepository;
    private final TutorAgeGroupJpaRepository tutorAgeGroupJpaRepository;
    private final ApplicationJpaRepository applicationJpaRepository;
    private final TutorScheduleJpaRepository tutorScheduleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final TutorMapper tutorMapper;

}
