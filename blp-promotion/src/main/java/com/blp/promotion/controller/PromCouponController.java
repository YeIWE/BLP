package com.blp.promotion.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blp.common.result.ApiResult;
import com.blp.common.result.PageResult;
import com.blp.promotion.entity.PromCoupon;
import com.blp.promotion.service.PromCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "优惠券管理")
@RestController
@RequestMapping("/promotion/coupon")
@RequiredArgsConstructor
public class PromCouponController {

    private final PromCouponService couponService;

    @Operation(summary = "新增优惠券")
    @PostMapping
    public ApiResult<Void> create(@RequestBody PromCoupon coupon) {
        couponService.save(coupon);
        return ApiResult.ok();
    }

    @Operation(summary = "优惠券列表")
    @GetMapping("/list")
    public ApiResult<PageResult<PromCoupon>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<PromCoupon> result = couponService.pageCoupons(page, size);
        return ApiResult.ok(PageResult.of(result.getTotal(), page, size, result.getRecords()));
    }

    @Operation(summary = "优惠券详情")
    @GetMapping("/{id}")
    public ApiResult<PromCoupon> detail(@PathVariable Long id) {
        return ApiResult.ok(couponService.getById(id));
    }

    @Operation(summary = "更新优惠券")
    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody PromCoupon coupon) {
        coupon.setId(id);
        couponService.updateById(coupon);
        return ApiResult.ok();
    }

    @Operation(summary = "删除优惠券")
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        couponService.removeById(id);
        return ApiResult.ok();
    }
}
