package inha.dayoook_e.tutor.api.controller.dto.response;

import inha.dayoook_e.mapping.api.controller.dto.response.SearchAgeGroupResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchCountryResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;
import inha.dayoook_e.user.domain.enums.KoreanLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TutorSearchPageResponse(

        @NotNull
        @Schema(description = "튜터 id", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "튜터 이름", example = "김튜터")
        String name,

        @NotNull
        @Schema(description = "튜터 평점", example = "4.5")
        Double rating,

        @NotNull
        @Schema(description = "한국어 수준", example = "BEGINNER")
        KoreanLevel koreanLevel,

        @NotNull
        List<SearchAgeGroupResponse> ageGroups,

        @NotNull
        List<SearchLanguagesResponse> languages

) {}

