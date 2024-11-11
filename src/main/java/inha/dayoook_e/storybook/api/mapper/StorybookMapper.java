package inha.dayoook_e.storybook.api.mapper;

import inha.dayoook_e.mapping.domain.Country;
import inha.dayoook_e.storybook.api.controller.dto.request.CreateStorybookRequest;
import inha.dayoook_e.storybook.api.controller.dto.response.LikedTuteeStorybookProgressResponse;
import inha.dayoook_e.storybook.api.controller.dto.response.StorybookResponse;
import inha.dayoook_e.storybook.api.controller.dto.response.StorybookSearchPageResponse;
import inha.dayoook_e.storybook.domain.Storybook;
import inha.dayoook_e.storybook.domain.StorybookPage;
import inha.dayoook_e.storybook.domain.TuteeStoryProgress;
import inha.dayoook_e.storybook.domain.id.TuteeStoryProgressId;
import inha.dayoook_e.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * StorybookMapper는 동화와 관련된 데이터 변환 기능을 제공.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StorybookMapper {

    /**
     * CreateStorybookRequest를 Storybook으로 변환.
     *
     * @param request     CreateStorybookRequest
     * @param country     Country
     * @param thumbnailUrl 썸네일 URL
     * @return Storybook
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pages", ignore = true)
    Storybook createStorybookRequestToStorybook(CreateStorybookRequest request, Country country, String thumbnailUrl);
    /**
     * CreateStorybookRequest.PageContent를 StorybookPage로 변환.
     *
     * @param pageContent  CreateStorybookRequest.PageContent
     * @param pageUrl      페이지 URL
     * @param storybook 저장된 Storybook
     * @return StorybookPage
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "storybook", ignore = true)  // 양방향 관계는 서비스에서 처리
    StorybookPage createStorybookPageRequestToStorybookPage(CreateStorybookRequest.PageContent pageContent, String pageUrl, Storybook storybook);

    /**
     * Storybook을 StorybookResponse로 변환.
     *
     * @param storybook Storybook
     * @return StorybookResponse
     */
    StorybookResponse storybookToStorybookResponse(Storybook storybook);

    /**
     * Storybook을 TuteeStoryProgress로 변환.
     *
     * @param user      User
     * @param storybook Storybook
     * @return TuteeStoryProgress
     */
    default TuteeStoryProgress toTuteeStoryProgress(User user, Storybook storybook) {
        return new TuteeStoryProgress(new TuteeStoryProgressId(user.getId(), storybook.getId()), false, 1,  false, user, storybook);
    }


    /**
     * TuteeStoryProgress를 LikedTuteeStorybookProgressResponse로 변환.
     *
     * @param tuteeStoryProgress TuteeStoryProgress
     * @return LikedTuteeStorybookProgressResponse
     */
    @Mapping(target = "id", source = "tuteeStoryProgress.id.storybookId")
    LikedTuteeStorybookProgressResponse tuteeStoryProgressToLikedTuteeStorybookProgressResponse(TuteeStoryProgress tuteeStoryProgress);

    /**
     * Storybook을 StorybookSearchPageResponse로 변환.
     *
     * @param storybook Storybook
     * @param liked     좋아요 누른 여부
     * @param lastPageNumber 마지막 페이지 번호
     * @param isCompleted 완료 여부
     * @return StorybookSearchPageResponse
     */
    StorybookSearchPageResponse storybookToStorybookSearchPageResponse(Storybook storybook, boolean liked, int lastPageNumber, boolean isCompleted);
}
