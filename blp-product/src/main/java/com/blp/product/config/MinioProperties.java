package com.blp.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private String endpoint = "http://localhost:9001";
    private String accessKey = "minio";
    private String secretKey = "minio";
    private String bucket = "blp-product";
}
