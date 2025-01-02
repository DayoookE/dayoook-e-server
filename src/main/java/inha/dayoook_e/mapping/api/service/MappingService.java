package inha.dayoook_e.mapping.api.service;

import inha.dayoook_e.mapping.api.controller.dto.response.*;

import java.util.List;

public interface MappingService {
    List<SearchLanguagesResponse> getLanguages();
    List<SearchCountryResponse> getCountries();
    List<SearchAgeGroupResponse> getAgeGroups();
    List<SearchDayResponse> getDays();
    List<SearchTimeSlotResponse> getTimeSlots();
}
