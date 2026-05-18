package com.blp.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blp.common.result.ApiResult;
import com.blp.common.result.PageResult;
import com.blp.order.entity.OmsOrder;
import com.blp.order.entity.OmsOrderItem;
import com.blp.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "订单管理")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "创建订单")
    @PostMapping
    public ApiResult<OmsOrder> create(@RequestHeader("X-User-Id") Long userId,
                                      @RequestBody Map<String, String> body) {
        OmsOrder order = orderService.createOrder(
            userId,
            body.get("receiverName"),
            body.get("receiverPhone"),
            body.get("receiverAddress")
        );
        return ApiResult.ok(order);
    }

    @Operation(summary = "分页查询订单")
    @GetMapping("/page")
    public ApiResult<PageResult<OmsOrder>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestParam(required = false) String status) {
        IPage<OmsOrder> result = orderService.pageOrders(page, size, userId, status);
        return ApiResult.ok(PageResult.of(result.getTotal(), page, size, result.getRecords()));
    }

    @Operation(summary = "订单详情")
    @GetMapping("/{id}")
    public ApiResult<Map<String, Object>> detail(@PathVariable Long id) {
        OmsOrder order = orderService.getOrderDetail(id);
        List<OmsOrderItem> items = orderService.getOrderItems(id);
        return ApiResult.ok(Map.of("order", order, "items", items));
    }

    @Operation(summary = "更新订单状态")
    @PutMapping("/{id}/status")
    public ApiResult<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateStatus(id, status);
        return ApiResult.ok();
    }
}
