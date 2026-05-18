package com.blp.log.repository;

import com.blp.log.entity.UserLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserLogRepository extends MongoRepository<UserLog, String> {
    Page<UserLog> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);
}
