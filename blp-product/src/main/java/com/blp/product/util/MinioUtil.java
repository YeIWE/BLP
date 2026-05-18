package com.blp.product.util;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MinioUtil {

    private final MinioClient minioClient;
    private final com.blp.product.config.MinioProperties props;

    public String upload(MultipartFile file) throws Exception {
        String bucket = props.getBucket();
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
        String ext = "";
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String objectName = UUID.randomUUID().toString() + ext;
        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(is, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        }
        return props.getEndpoint() + "/" + bucket + "/" + objectName;
    }
}
