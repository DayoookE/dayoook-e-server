package inha.dayoook_e.storybook.api.service;

import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.mapping.domain.Country;
import inha.dayoook_e.mapping.domain.repository.CountryJpaRepository;
import inha.dayoook_e.storybook.api.controller.dto.request.CreateStorybookRequest;
import inha.dayoook_e.storybook.api.controller.dto.response.LikedTuteeStorybookProgressResponse;
import inha.dayoook_e.storybook.api.controller.dto.response.StorybookResponse;
import inha.dayoook_e.storybook.api.mapper.StorybookMapper;
import inha.dayoook_e.storybook.domain.Storybook;
import inha.dayoook_e.storybook.domain.StorybookPage;
import inha.dayoook_e.storybook.domain.TuteeStoryProgress;
import inha.dayoook_e.storybook.domain.repository.StorybookJpaRepository;
import inha.dayoook_e.storybook.domain.repository.StorybookPageJpaRepository;
import inha.dayoook_e.storybook.domain.repository.TuteeStoryProgressJpaRepository;
import inha.dayoook_e.user.domain.User;
import inha.dayoook_e.utils.s3.S3Provider;
import inha.dayoook_e.utils.s3.dto.request.S3UploadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static inha.dayoook_e.common.Constant.STORYBOOK_PAGE_DIR;
import static inha.dayoook_e.common.Constant.STORYBOOK_THUMBNAIL_DIR;
import static inha.dayoook_e.common.code.status.ErrorStatus.*;

/**
 * StorybookServiceImpl은 동화 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StorybookServiceImpl implements StorybookService {


    private final StorybookJpaRepository storybookJpaRepository;
    private final StorybookPageJpaRepository storybookPageJpaRepository;
    private final TuteeStoryProgressJpaRepository tuteeStoryProgressJpaRepository;
    private final CountryJpaRepository countryJpaRepository;
    private final StorybookMapper storybookMapper;
    private final S3Provider s3Provider;

    /**
     * 동화 생성
     *
     * @param user 로그인한 사용자
     * @param createStorybookRequest 동화 생성 요청
     * @param thumbnail 썸네일 이미지
     * @param pageImages 페이지 이미지 리스트
     * @return 동화 생성 결과
     */
    @Override
    public StorybookResponse createStorybook(User user, CreateStorybookRequest createStorybookRequest,
                                             MultipartFile thumbnail, List<MultipartFile> pageImages) {
        // 1. 요청 데이터 검증
        if (pageImages.size() != createStorybookRequest.pageContents().size()) {
            throw new BaseException(PAGE_COUNT_MISMATCH);
        }

        // 2. 국가 조회
        Country country = countryJpaRepository.findById(createStorybookRequest.countryId())
                .orElseThrow(() -> new BaseException(COUNTRY_NOT_FOUND));

        // 3. 섬네일 업로드
        String thumbnailUrl = s3Provider.multipartFileUpload(thumbnail, new S3UploadRequest(user.getId(), STORYBOOK_THUMBNAIL_DIR));

        // 4. Storybook 엔티티 생성 및 저장
        Storybook storybook = storybookMapper.createStorybookRequestToStorybook(createStorybookRequest, country, thumbnailUrl);

        // 5. 페이지 이미지 업로드 및 StorybookPage 엔티티 생성
        for (int i = 0; i < pageImages.size(); i++) {
            MultipartFile pageImage = pageImages.get(i);
            CreateStorybookRequest.PageContent pageContent = createStorybookRequest.pageContents().get(i);

            String pageUrl = s3Provider.multipartFileUpload(
                    pageImage,
                    new S3UploadRequest(user.getId(), STORYBOOK_PAGE_DIR)
            );

            StorybookPage page = storybookMapper.createStorybookPageRequestToStorybookPage(
                    pageContent,
                    pageUrl,
                    storybook
            );

            // 연관관계 설정
            storybook.addPage(page);
            storybookPageJpaRepository.save(page);
        }

        // 6. 모든 관계가 설정된 storybook 저장
        Storybook savedStorybook = storybookJpaRepository.save(storybook);
        return storybookMapper.storybookToStorybookResponse(savedStorybook);
    }

    /**
     * 좋아요 토글
     *
     * @param user 로그인한 사용자
     * @param storybookId 동화 ID
     * @return 좋아요 토글 결과
     */
    @Override
    public LikedTuteeStorybookProgressResponse toggleLike(User user, Integer storybookId) {
        // 1. 동화 조회
        Storybook storybook = storybookJpaRepository.findById(storybookId)
                .orElseThrow(() -> new BaseException(STORYBOOK_NOT_FOUND));

        // 2. 사용자의 동화 진행상황 조회 또는 생성
        TuteeStoryProgress progress = tuteeStoryProgressJpaRepository
                .findByTutee_IdAndStorybook_Id(user.getId(), storybookId)
                .orElse(storybookMapper.toTuteeStoryProgress(user, storybook));

        // 3. 좋아요 토글
        progress.toggleLike();

        tuteeStoryProgressJpaRepository.save(progress);
        return storybookMapper.tuteeStoryProgressToLikedTuteeStorybookProgressResponse(progress);
    }
}
