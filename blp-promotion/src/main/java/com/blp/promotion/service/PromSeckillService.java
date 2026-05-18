package com.blp.promotion.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blp.promotion.entity.PromSeckill;

public interface PromSeckillService extends IService<PromSeckill> {
    IPage<PromSeckill> pageSeckill(int page, int size);
    void preloadStockToRedis(Long seckillId);
    boolean executeSeckill(Long seckillId, Long userId);
}
