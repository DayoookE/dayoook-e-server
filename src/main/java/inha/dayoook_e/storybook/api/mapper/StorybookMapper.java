package inha.dayoook_e.storybook.api.mapper;

import inha.dayoook_e.mapping.domain.Country;
import inha.dayoook_e.storybook.api.controller.dto.request.CreateStorybookRequest;
import inha.dayoook_e.storybook.api.controller.dto.response.StorybookResponse;
import inha.dayoook_e.storybook.domain.Storybook;
import inha.dayoook_e.storybook.domain.StorybookPage;
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
}
