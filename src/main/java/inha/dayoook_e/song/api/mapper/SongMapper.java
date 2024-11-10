package inha.dayoook_e.song.api.mapper;

import inha.dayoook_e.mapping.api.dto.response.SearchCountryResponse;
import inha.dayoook_e.mapping.domain.Country;
import inha.dayoook_e.song.api.controller.dto.request.CreateSongRequest;
import inha.dayoook_e.song.api.controller.dto.response.SongResponse;
import inha.dayoook_e.song.api.controller.dto.response.SongSearchPageResponse;
import inha.dayoook_e.song.api.controller.dto.response.SongSearchResponse;
import inha.dayoook_e.song.domain.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * SongMapper는 동요와 관련된 데이터 변환 기능을 제공.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SongMapper {

    /**
     * CreateSongRequest를 Song 엔티티로 변환
     *
     * @param createSongRequest 동요 생성 요청 정보
     * @param country           국가 정보
     * @param thumbnailUrl      썸네일 URL
     * @param mediaUrl          미디어 URL
     * @return Song
     */
    @Mapping(target = "id", ignore = true)
    Song createSongRequestToSong(CreateSongRequest createSongRequest, Country country, String thumbnailUrl, String mediaUrl);

    /**
     * Song 엔티티를 SongResponse로 변환
     *
     * @param savedSong 저장된 동요 정보
     * @return SongResponse
     */
    SongResponse songToSongResponse(Song savedSong);

    /**
     * Song 엔티티를 SongSearchPageResponse로 변환
     *
     * @param song      동요 정보
     * @param liked     좋아요 누른 여부
     * @param completed 완료 여부
     * @return SongSearchPageResponse
     */
    SongSearchPageResponse songToSongSearchPageResponse(Song song, boolean liked, boolean completed);

    /**
     * Song 엔티티를 SongSearchResponse로 변환
     *
     * @param song            동요 정보
     * @param countryResponse 국가 정보
     * @param liked           좋아요 누른 여부
     * @param completed       완료 여부
     * @return SongSearchResponse
     */
    @Mapping(target = "id", source = "song.id")
    SongSearchResponse songToSongSearchResponse(Song song, SearchCountryResponse countryResponse, boolean liked, boolean completed);
}
