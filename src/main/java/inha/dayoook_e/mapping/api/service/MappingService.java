package inha.dayoook_e.mapping.api.service;

import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;

import java.util.List;

public interface MappingService {
    List<SearchLanguagesResponse> getLanguages();

}
