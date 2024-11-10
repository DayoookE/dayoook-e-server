package inha.dayoook_e.mapping.api.service;

import inha.dayoook_e.mapping.api.controller.dto.response.SearchAgeGroupResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchCountryResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;

import java.util.List;

public interface MappingService {
    List<SearchLanguagesResponse> getLanguages();
    List<SearchCountryResponse> getCountries();
    List<SearchAgeGroupResponse> getAgeGroups();

}
