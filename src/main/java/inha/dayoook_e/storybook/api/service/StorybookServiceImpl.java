package inha.dayoook_e.storybook.api.service;

import inha.dayoook_e.storybook.api.mapper.StorybookMapper;
import inha.dayoook_e.storybook.domain.repository.StorybookJpaRepository;
import inha.dayoook_e.storybook.domain.repository.StorybookPageJpaRepository;
import inha.dayoook_e.storybook.domain.repository.TuteeStoryProgressJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * StorybookServiceImpl은 동화 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StorybookServiceImpl implements StorybookService {


    private final StorybookJpaRepository storybookJpaRepository;
    private final StorybookPageJpaRepository storybookPageJpaRepository;
    private final TuteeStoryProgressJpaRepository tuteeStoryProgressJpaRepository;
    private final StorybookMapper storybookMapper;

}
