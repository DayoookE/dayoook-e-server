package inha.dayoook_e.mapping.api.mapper;

import inha.dayoook_e.mapping.api.controller.dto.response.*;
import inha.dayoook_e.tutor.api.controller.dto.request.ScheduleTimeSlot;
import inha.dayoook_e.tutor.domain.TutorAgeGroup;
import inha.dayoook_e.user.domain.Point;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.user.domain.UserLanguage;
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
     * 연령대 조회 응답 DTO 생성
     *
     * @param id 연령대 ID
     * @param name 연령대 이름
     * @return 연령대 조회 응답 DTO
     */
    SearchAgeGroupResponse toSearchAgeGroupResponse(Integer id, String name);

    /**
     * 언어 조회 응답 DTO 생성
     *
     * @param id 언어 ID
     * @param name 언어 이름
     * @return 언어 조회 응답 DTO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SearchLanguagesResponse toSearchLanguagesResponse(Integer id, String name);

    /**
     * 요일 조회 응답 DTO 생성
     *
     * @param id 요일 ID
     * @param name 요일 이름
     * @return 요일 조회 응답 DTO
     */
    SearchDayResponse toSearchDayResponse(Integer id, String name);

    /**
     * 시간대 조회 응답 DTO 생성
     *
     * @param id 시간대 ID
     * @param time 시간대
     * @return 시간대 조회 응답 DTO
     */
    SearchTimeSlotResponse  toSearchTimeSlotResponse(Integer id, String time);

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

    /**
     * 연령대 조회 응답 Dto 생성
     *
     * @param tutorAgeGroup 튜터 연령대 정보
     * @return 연령대 조회 응답 Dto
     */
    @Mapping(target = "id", source = "tutorAgeGroup.ageGroup.id")
    @Mapping(target = "name", source = "tutorAgeGroup.ageGroup.name")
    SearchAgeGroupResponse toSearchAgeGroupResponse(TutorAgeGroup tutorAgeGroup);

    /**
     * UserLanguage를 언어 검색 응답 Dto로 변환
     *
     * @param userLanguage 유저의 언어 정보
     * @return 언어 검색 응답
     */
    @Mapping(source = "userLanguage.language.id", target = "id")
    @Mapping(source = "userLanguage.language.name", target = "name")
    SearchLanguagesResponse userLanguageToSearchLanguageResponse(UserLanguage userLanguage);

    /**
     * ScheduleTimeSlot을 ScheduleTimeSlot으로 변환
     *
     * @param dayId 요일 ID
     * @param timeSlotId 시간대 ID
     * @return ScheduleTimeSlot
     */
    ScheduleTimeSlot toScheduleTimeSlot(Integer dayId, Integer timeSlotId);

}
