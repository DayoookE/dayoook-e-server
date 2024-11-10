package inha.dayoook_e.song.api.service;

import inha.dayoook_e.song.api.controller.dto.request.CreateSongRequest;
import inha.dayoook_e.song.api.controller.dto.response.SongResponse;
import inha.dayoook_e.song.api.controller.dto.response.SongSearchPageResponse;
import inha.dayoook_e.user.domain.User;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

public interface SongService {

    Slice<SongSearchPageResponse> getSongs(User user, Integer countryId, Integer page);
    SongResponse createSong(User user, CreateSongRequest createSongRequest, MultipartFile thumbnail, MultipartFile media);


}
