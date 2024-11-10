package inha.dayoook_e.utils.s3.dto.request;

public record S3UploadRequest(
        Integer userId,
        String dirName
) {
}
