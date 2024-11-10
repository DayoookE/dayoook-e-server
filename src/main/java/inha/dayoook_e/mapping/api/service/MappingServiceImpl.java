package inha.dayoook_e.mapping.api.service;

import inha.dayoook_e.mapping.api.controller.dto.response.*;
import inha.dayoook_e.mapping.api.mapper.MappingMapper;
import inha.dayoook_e.mapping.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * MappingServiceImpl은 매핑 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MappingServiceImpl implements MappingService{

    private final LanguageJpaRepository languageJpaRepository;
    private final CountryJpaRepository countryJpaRepository;
    private final AgeGroupJpaRepository ageGroupJpaRepository;
    private final DayJpaRepository dayJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final MappingMapper mappingMapper;

    /**
     * 언어 목록을 조회합니다.
     *
     * @return 언어 목록
     */
    @Override
    public List<SearchLanguagesResponse> getLanguages() {
        return languageJpaRepository.findAll()
                .stream()
                .map(language -> mappingMapper.toSearchLanguagesResponse(language.getId(), language.getName()))
                .toList();
    }

    /**
     * 국가 목록을 조회합니다.
     *
     * @return 국가 목록
     */
    @Override
    public List<SearchCountryResponse> getCountries() {
        return countryJpaRepository.findAll()
                .stream()
                .map(country -> mappingMapper.toSearchCountryResponse(country.getId(), country.getName(), country.getFlagUrl()))
                .toList();
    }

    /**
     * 연령대 목록을 조회합니다.
     *
     * @return 연령대 목록
     */
    @Override
    public List<SearchAgeGroupResponse> getAgeGroups() {
        return ageGroupJpaRepository.findAll()
                .stream()
                .map(ageGroup -> mappingMapper.toSearchAgeGroupResponse(ageGroup.getId(), ageGroup.getName()))
                .toList();
    }

    /**
     * 요일 목록을 조회합니다.
     *
     * @return 요일 목록
     */
    @Override
    public List<SearchDayResponse> getDays() {
        return dayJpaRepository.findAll()
                .stream()
                .map(day -> mappingMapper.toSearchDayResponse(day.getId(), day.getName()))
                .toList();
    }

    /**
     * 시간대 목록을 조회합니다.
     *
     * @return 시간대 목록
     */
    @Override
    public List<SearchTimeSlotResponse> getTimeSlots() {
        return timeSlotJpaRepository.findAll()
                .stream()
                .map(timeSlot -> mappingMapper.toSearchTimeSlotResponse(timeSlot.getId(), timeSlot.getTime()))
                .toList();
    }
}
