package inha.dayoook_e.tutee.api.service;

import inha.dayoook_e.application.domain.ApplicationGroup;
import inha.dayoook_e.application.domain.enums.Status;
import inha.dayoook_e.application.domain.repository.ApplicationGroupJpaRepository;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;
import inha.dayoook_e.mapping.api.mapper.MappingMapper;
import inha.dayoook_e.tutee.api.controller.dto.response.SearchTuteeApplicationResponse;
import inha.dayoook_e.tutee.api.mapper.TuteeMapper;
import inha.dayoook_e.tutee.domain.repository.TuteeInfoJpaRepository;
import inha.dayoook_e.tutor.api.controller.dto.request.ScheduleTimeSlot;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import inha.dayoook_e.user.domain.repository.UserLanguageJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;
import static inha.dayoook_e.common.Constant.CREATE_AT;

/**
 * TuteeServiceImpl은 튜티 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TuteeServiceImpl implements TuteeService {


    private final TuteeMapper tuteeMapper;
    private final ApplicationGroupJpaRepository applicationGroupJpaRepository;
    private final MappingMapper mappingMapper;

    @Override
    public Slice<SearchTuteeApplicationResponse> getTuteeApplications(User user, Integer page, Status status) {
        PageRequest pageRequest = PageRequest.of(page, 10 + 1, Sort.by(Sort.Direction.DESC, CREATE_AT));

        Slice<ApplicationGroup> applications;
        if (status != null) {
            applications = applicationGroupJpaRepository
                    .findSliceByTuteeAndStatusAndState(user, status, ACTIVE, pageRequest);
        } else {
            applications = applicationGroupJpaRepository
                    .findSliceByTuteeAndState(user, ACTIVE, pageRequest);
        }

        return applications.map(applicationGroup -> {
            List<SearchLanguagesResponse> languages = applicationGroup.getTutor().getUserLanguages().stream()
                    .map(userLanguage -> mappingMapper.toSearchLanguagesResponse(
                            userLanguage.getLanguage().getId(),
                            userLanguage.getLanguage().getName()
                    ))
                    .toList();

            List<ScheduleTimeSlot> scheduleTimeSlots = applicationGroup.getApplications().stream()
                    .map(application -> mappingMapper.toScheduleTimeSlot(
                            application.getDay().getId(),
                            application.getTimeSlot().getId()
                    ))
                    .toList();

            return tuteeMapper.toSearchTuteeApplicationResponse(applicationGroup, languages, scheduleTimeSlots);
        });
    }
}
