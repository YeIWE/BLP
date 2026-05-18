package com.blp.review.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blp.common.result.ApiResult;
import com.blp.common.result.PageResult;
import com.blp.review.entity.PmsReview;
import com.blp.review.service.PmsReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "商品评价")
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class PmsReviewController {

    private final PmsReviewService reviewService;

    @Operation(summary = "新增评价")
    @PostMapping
    public ApiResult<Void> create(@RequestBody PmsReview review) {
        reviewService.save(review);
        return ApiResult.ok();
    }

    @Operation(summary = "分页查询评价")
    @GetMapping("/page")
    public ApiResult<PageResult<PmsReview>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long productId) {
        IPage<PmsReview> result = reviewService.pageByProduct(page, size, productId);
        return ApiResult.ok(PageResult.of(result.getTotal(), page, size, result.getRecords()));
    }

    @Operation(summary = "评价统计")
    @GetMapping("/stats/{productId}")
    public ApiResult<Map<String, Object>> stats(@PathVariable Long productId) {
        return ApiResult.ok(reviewService.stats(productId));
    }
}
