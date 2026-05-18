package com.blp.inventory.service;

import com.blp.common.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private static final String STOCK_KEY_PREFIX = "inventory:stock:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final DefaultRedisScript<Long> deductStockScript;

    public Integer getStock(Long productId) {
        String key = STOCK_KEY_PREFIX + productId;
        Object val = redisTemplate.opsForValue().get(key);
        if (val == null) {
            return 0;
        }
        return Integer.parseInt(val.toString());
    }

    public Long deductStock(Long productId, Integer count) {
        String key = STOCK_KEY_PREFIX + productId;
        Long result = redisTemplate.execute(
            deductStockScript,
            Collections.singletonList(key),
            String.valueOf(count)
        );
        if (result != null && result == -1) {
            throw new BizException("库存不足");
        }
        return result;
    }

    public void setStock(Long productId, Integer stock) {
        String key = STOCK_KEY_PREFIX + productId;
        redisTemplate.opsForValue().set(key, stock);
    }

    public void batchSetStock(List<Long> productIds) {
        // Initialize stock from MySQL — called on startup or when cache is cold
        // In a real system, this would query pms_product.stock
        for (Long productId : productIds) {
            String key = STOCK_KEY_PREFIX + productId;
            // Default to 100 if cache miss (sync from DB in real implementation)
            redisTemplate.opsForValue().setIfAbsent(key, 100);
        }
    }
}
