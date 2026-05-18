package com.blp.log.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "user_logs")
public class UserLog {
    @Id
    private String id;
    private Long userId;
    private String action;
    private String target;
    private LocalDateTime timestamp;
    private String ip;
    private String userAgent;
}
