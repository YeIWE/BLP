package com.blp.log.service;

import com.blp.log.entity.UserLog;
import org.springframework.data.domain.Page;

public interface LogService {
    void saveLog(UserLog log);
    Page<UserLog> pageLogs(int page, int size, Long userId);
}
