package inha.dayoook_e.tutee.api.service;

import inha.dayoook_e.tutee.api.mapper.TuteeMapper;
import inha.dayoook_e.tutee.domain.repository.TuteeInfoJpaRepository;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TuteeServiceImpl은 튜티 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TuteeServiceImpl implements TuteeService {


    private final TuteeInfoJpaRepository tuteeInfoJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final TuteeMapper tuteeMapper;

}
