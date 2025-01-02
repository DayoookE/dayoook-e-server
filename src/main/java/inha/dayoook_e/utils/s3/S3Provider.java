package inha.dayoook_e.utils.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.utils.s3.dto.request.S3UploadRequest;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static inha.dayoook_e.common.Constant.PROFILE_IMAGE_DIR;
import static inha.dayoook_e.common.code.status.ErrorStatus.FILE_CONVERT_ERROR;
import static inha.dayoook_e.common.code.status.ErrorStatus.S3_UPLOAD_ERROR;


/**
 * S3Provider는 AWS S3에 파일을 업로드하는 기능을 제공.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class S3Provider {
    private final AmazonS3 amazonS3Client;
    private TransferManager transferManager;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * TransferManager 초기화 메서드.
     * 이 메서드는 S3 클라이언트를 사용하여 TransferManager를 초기화.
     */
    @PostConstruct
    public void init() {
        this.transferManager = TransferManagerBuilder.standard()
                .withS3Client(amazonS3Client)
                .build();
    }

    /**
     * TransferManager 종료 메서드.
     * 애플리케이션 종료 시 TransferManager를 안전하게 종료.
     */
    @PreDestroy
    public void shutdown() {
        if (this.transferManager != null) {
            this.transferManager.shutdownNow();
        }
    }

    /**
     * 파일을 S3에 업로드하는 메서드.
     *
     * @param file    업로드할 파일
     * @param s3UploadRequest S3 업로드 요청 정보
     *
     * @return 업로드된 파일의 URL
     */
    public String multipartFileUpload(MultipartFile file, S3UploadRequest s3UploadRequest) {
        String fileName = s3UploadRequest.dirName() + "/" + s3UploadRequest.userId() + "/" + UUID.randomUUID();

        try {
            InputStream is = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());
            Upload upload = transferManager.upload(bucket, fileName, is, objectMetadata);
            try {
                upload.waitForCompletion();
            } catch (InterruptedException e) {
                log.error("S3 multipartFileUpload error", e);
                throw new BaseException(S3_UPLOAD_ERROR);
            }
        } catch (IOException e) {
            log.error("File convert error", e);
            throw new BaseException(FILE_CONVERT_ERROR);
        }
        return "/" + fileName;
    }

}