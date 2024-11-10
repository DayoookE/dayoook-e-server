package inha.dayoook_e.mapping.api.controller;

import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;
import inha.dayoook_e.mapping.api.service.MappingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static inha.dayoook_e.common.code.status.SuccessStatus.LANGUAGES_SEARCH_OK;

/**
 * MappingController은 매핑 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "mapping controller", description = "매핑 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MappingController {

    private final MappingService mappingService;

    /**
     * 언어 목록 조회 API
     *
     * @return 언어 목록
     */
    @GetMapping("/languages")
    @Operation(summary = "언어 목록 조회 API", description = "언어 목록을 조회합니다.")
    public BaseResponse<List<SearchLanguagesResponse>> getLanguages() {
        return BaseResponse.of(LANGUAGES_SEARCH_OK, mappingService.getLanguages());
    }
}
