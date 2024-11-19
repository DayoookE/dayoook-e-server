package inha.dayoook_e.tutor.api.controller.dto.response;

import inha.dayoook_e.mapping.api.controller.dto.response.SearchAgeGroupResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchCountryResponse;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchLanguagesResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

public record TutorSearchResponse(

    @NotNull
    @Schema(description = "튜터 id", example = "1")
    Integer id,

    @NotNull
    @Schema(description = "튜터 이름", example = "김튜터")
    String name,

    @NotNull
    @Schema(description = "튜터 직업", example = "교수")
    String occupation,

    @NotNull
    @Schema(description = "튜터 소개", example = "안녕하세요")
    String introduction,

    @NotNull
    @Schema(description = "튜터 평점", example = "4.5")
    Double rating,

    @NotNull
    List<SearchAgeGroupResponse> ageGroups,

    @NotNull
    List<SearchLanguagesResponse>  languages,

    @NotNull
    List<SearchExperienceResponse> experiences
) {}

