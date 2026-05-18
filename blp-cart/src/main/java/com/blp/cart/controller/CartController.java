package com.blp.cart.controller;

import com.blp.cart.entity.CartItem;
import com.blp.cart.service.CartService;
import com.blp.common.result.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "购物车管理")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "获取购物车列表")
    @GetMapping
    public ApiResult<List<CartItem>> list(@RequestHeader("X-User-Id") Long userId) {
        return ApiResult.ok(cartService.listCart(userId));
    }

    @Operation(summary = "添加商品到购物车")
    @PostMapping
    public ApiResult<Void> add(@RequestHeader("X-User-Id") Long userId, @RequestBody CartItem item) {
        cartService.addItem(userId, item);
        return ApiResult.ok();
    }

    @Operation(summary = "更新购物车商品数量")
    @PutMapping("/{productId}")
    public ApiResult<Void> update(@RequestHeader("X-User-Id") Long userId,
                                  @PathVariable Long productId,
                                  @RequestParam Integer quantity) {
        cartService.updateQuantity(userId, productId, quantity);
        return ApiResult.ok();
    }

    @Operation(summary = "删除购物车商品")
    @DeleteMapping("/{productId}")
    public ApiResult<Void> remove(@RequestHeader("X-User-Id") Long userId,
                                  @PathVariable Long productId) {
        cartService.removeItem(userId, productId);
        return ApiResult.ok();
    }

    @Operation(summary = "清空购物车")
    @DeleteMapping
    public ApiResult<Void> clear(@RequestHeader("X-User-Id") Long userId) {
        cartService.clearCart(userId);
        return ApiResult.ok();
    }
}
