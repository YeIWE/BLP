package com.blp.log.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.blp.log.entity.UserLog;
import com.blp.log.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final LogService logService;

    @KafkaListener(topics = "blp-logs", groupId = "blp-log-group")
    public void consume(String message) {
        try {
            JSONObject json = JSONUtil.parseObj(message);
            UserLog userLog = new UserLog();
            userLog.setUserId(json.getLong("userId"));
            userLog.setAction(json.getStr("action"));
            userLog.setTarget(json.getStr("target"));
            userLog.setTimestamp(LocalDateTime.now());
            userLog.setIp(json.getStr("ip"));
            userLog.setUserAgent(json.getStr("userAgent"));
            logService.saveLog(userLog);
        } catch (Exception e) {
            log.error("Failed to consume log message: {}", message, e);
        }
    }
}
