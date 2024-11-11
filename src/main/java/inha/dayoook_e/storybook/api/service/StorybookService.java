package inha.dayoook_e.storybook.api.service;

import inha.dayoook_e.storybook.api.controller.dto.request.SearchCond;
import inha.dayoook_e.storybook.api.controller.dto.request.CreateStorybookRequest;
import inha.dayoook_e.storybook.api.controller.dto.response.*;
import inha.dayoook_e.user.domain.User;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorybookService {
    Slice<StorybookSearchPageResponse> getStorybooks(User user, SearchCond searchCond, Integer page);
    StorybookSearchResponse getStorybook(User user, Integer storybookId, Integer pageNumber);
    StorybookResponse createStorybook(User user, CreateStorybookRequest createStorybookRequest, MultipartFile thumbnail, List<MultipartFile> pageImages);
    LikedTuteeStorybookProgressResponse toggleLike(User user, Integer storybookId);
    StorybookResponse completeStorybook(User user, Integer storybookId);
    LastReadPageStorybookResponse updateLastReadPage(User user, Integer storybookId, Integer pageNumber);
}
