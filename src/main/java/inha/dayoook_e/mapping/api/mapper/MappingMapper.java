package inha.dayoook_e.mapping.api.mapper;

import inha.dayoook_e.mapping.api.dto.response.SearchCountryResponse;
import inha.dayoook_e.user.domain.Point;
import inha.dayoook_e.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;

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

    /**
     * 포인트 생성
     *
     * @param user 유저
     * @param point 포인트
     * @param reason 포인트 적립/차감 사유
     * @param createdAt 포인트 적립/차감 날짜
     * @return 포인트 엔티티
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Point createPoint(User user, Integer point, String reason, LocalDateTime createdAt);
}
