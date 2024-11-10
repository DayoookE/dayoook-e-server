package inha.dayoook_e.mapping.api.service;

import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;
import inha.dayoook_e.mapping.api.mapper.MappingMapper;
import inha.dayoook_e.mapping.domain.repository.LanguageJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;

/**
 * MappingServiceImpl은 매핑 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MappingServiceImpl implements MappingService{

    private final LanguageJpaRepository languageJpaRepository;
    private final MappingMapper mappingMapper;

    /**
     * 언어 목록을 조회합니다.
     *
     * @return 언어 목록
     */
    @Override
    public List<SearchLanguagesResponse> getLanguages() {
        return languageJpaRepository.findAllByState(ACTIVE)
                .stream()
                .map(language -> mappingMapper.toSearchLanguagesResponse(language.id(), language.name()))
                .toList();
    }
}
