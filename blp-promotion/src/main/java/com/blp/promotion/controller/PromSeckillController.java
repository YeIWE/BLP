package com.blp.promotion.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blp.common.result.ApiResult;
import com.blp.common.result.PageResult;
import com.blp.promotion.entity.PromSeckill;
import com.blp.promotion.service.PromSeckillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "秒杀管理")
@RestController
@RequestMapping("/promotion/seckill")
@RequiredArgsConstructor
public class PromSeckillController {

    private final PromSeckillService seckillService;

    @Operation(summary = "创建秒杀活动")
    @PostMapping
    public ApiResult<PromSeckill> create(@RequestBody PromSeckill seckill) {
        seckillService.save(seckill);
        seckillService.preloadStockToRedis(seckill.getId());
        return ApiResult.ok(seckill);
    }

    @Operation(summary = "秒杀活动列表")
    @GetMapping("/list")
    public ApiResult<PageResult<PromSeckill>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<PromSeckill> result = seckillService.pageSeckill(page, size);
        return ApiResult.ok(PageResult.of(result.getTotal(), page, size, result.getRecords()));
    }

    @Operation(summary = "执行秒杀")
    @PostMapping("/{id}/buy")
    public ApiResult<Map<String, Object>> buy(
            @PathVariable Long id,
            @RequestParam Long userId) {
        boolean success = seckillService.executeSeckill(id, userId);
        if (success) {
            return ApiResult.ok(Map.of("result", "秒杀成功，订单处理中"));
        }
        return ApiResult.fail("秒杀失败，库存不足");
    }
}
