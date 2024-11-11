package inha.dayoook_e.storybook.api.service;

import inha.dayoook_e.storybook.api.controller.dto.request.CreateStorybookRequest;
import inha.dayoook_e.storybook.api.controller.dto.response.LikedTuteeStorybookProgressResponse;
import inha.dayoook_e.storybook.api.controller.dto.response.StorybookResponse;
import inha.dayoook_e.user.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorybookService {
    StorybookResponse createStorybook(User user, CreateStorybookRequest createStorybookRequest, MultipartFile thumbnail, List<MultipartFile> pageImages);

    LikedTuteeStorybookProgressResponse toggleLike(User user, Integer storybookId);
}
