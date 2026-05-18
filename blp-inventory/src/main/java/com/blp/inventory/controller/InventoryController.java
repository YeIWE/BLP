package com.blp.inventory.controller;

import com.blp.common.result.ApiResult;
import com.blp.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "库存管理")
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(summary = "查询库存")
    @GetMapping("/{productId}")
    public ApiResult<Integer> getStock(@PathVariable Long productId) {
        return ApiResult.ok(inventoryService.getStock(productId));
    }

    @Operation(summary = "扣减库存")
    @PutMapping("/{productId}/deduct")
    public ApiResult<Long> deduct(@PathVariable Long productId, @RequestParam Integer count) {
        Long remain = inventoryService.deductStock(productId, count);
        return ApiResult.ok(remain);
    }
}
