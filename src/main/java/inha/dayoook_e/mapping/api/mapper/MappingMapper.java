package inha.dayoook_e.mapping.api.mapper;

import inha.dayoook_e.mapping.api.dto.response.SearchCountryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MappingMapper는 매핑과 관련된 데이터 변환 기능을 제공.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MappingMapper {

    /**
     * 국가 조회 응답 DTO 생성
     *
     * @param id 국가 ID
     * @param name 국가 이름
     * @param flagUrl 국기 URL
     * @return 국가 조회 응답 DTO
     */
    SearchCountryResponse toSearchCountryResponse(Integer id, String name, String flagUrl);
}
