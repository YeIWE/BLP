package com.blp.log.service.impl;

import com.blp.log.entity.UserLog;
import com.blp.log.repository.UserLogRepository;
import com.blp.log.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final UserLogRepository logRepository;

    @Override
    public void saveLog(UserLog userLog) {
        logRepository.save(userLog);
        log.debug("Log saved: userId={}, action={}", userLog.getUserId(), userLog.getAction());
    }

    @Override
    public Page<UserLog> pageLogs(int page, int size, Long userId) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        if (userId != null) {
            return logRepository.findByUserIdOrderByTimestampDesc(userId, pageable);
        }
        return logRepository.findAll(pageable);
    }
}
