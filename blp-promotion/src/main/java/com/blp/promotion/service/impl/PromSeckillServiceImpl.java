package com.blp.promotion.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blp.common.exception.BizException;
import com.blp.promotion.entity.PromSeckill;
import com.blp.promotion.mapper.PromSeckillMapper;
import com.blp.promotion.service.PromSeckillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromSeckillServiceImpl extends ServiceImpl<PromSeckillMapper, PromSeckill> implements PromSeckillService {

    private final StringRedisTemplate redisTemplate;
    private final RabbitTemplate rabbitTemplate;

    private static final String SECKILL_STOCK_KEY = "seckill:stock:";
    private static final String SECKILL_LUA_SCRIPT =
            "local key = KEYS[1]\n" +
            "local stock = tonumber(redis.call('get', key))\n" +
            "if stock == nil or stock <= 0 then\n" +
            "    return -1\n" +
            "end\n" +
            "redis.call('decr', key)\n" +
            "return stock - 1";

    @Override
    public IPage<PromSeckill> pageSeckill(int page, int size) {
        LambdaQueryWrapper<PromSeckill> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(PromSeckill::getId);
        return page(new Page<>(page, size), wrapper);
    }

    @Override
    public void preloadStockToRedis(Long seckillId) {
        PromSeckill seckill = getById(seckillId);
        if (seckill == null) {
            throw new BizException("秒杀活动不存在");
        }
        String key = SECKILL_STOCK_KEY + seckillId;
        redisTemplate.opsForValue().set(key, String.valueOf(seckill.getStock()));
        log.info("预加载秒杀库存到Redis: seckillId={}, stock={}", seckillId, seckill.getStock());
    }

    @Override
    public boolean executeSeckill(Long seckillId, Long userId) {
        // 校验活动状态
        PromSeckill seckill = getById(seckillId);
        if (seckill == null || seckill.getStatus() != 1) {
            throw new BizException("秒杀活动不存在或已结束");
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(seckill.getStartTime()) || now.isAfter(seckill.getEndTime())) {
            throw new BizException("不在秒杀时间范围内");
        }

        // Lua 脚本原子扣减库存
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(SECKILL_LUA_SCRIPT, Long.class);
        Long result = redisTemplate.execute(script,
                Collections.singletonList(SECKILL_STOCK_KEY + seckillId));
        if (result == null || result < 0) {
            return false;
        }

        // 异步发送订单到 RabbitMQ
        Map<String, Object> orderMsg = Map.of(
                "seckillId", seckillId,
                "userId", userId,
                "productId", seckill.getProductId(),
                "price", seckill.getSeckillPrice(),
                "createTime", LocalDateTime.now().toString()
        );
        rabbitTemplate.convertAndSend("seckill.orders", JSONUtil.toJsonStr(orderMsg));
        log.info("秒杀成功，订单消息已发送: seckillId={}, userId={}", seckillId, userId);
        return true;
    }
}
