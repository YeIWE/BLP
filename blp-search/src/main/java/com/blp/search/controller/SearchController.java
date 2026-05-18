package com.blp.search.controller;

import com.blp.common.result.ApiResult;
import com.blp.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@Tag(name = "商品搜索")
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "搜索商品")
    @GetMapping
    public ApiResult<Map<String, Object>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResult.ok(searchService.search(keyword, categoryId, minPrice, maxPrice, page, size));
    }

    @Operation(summary = "手动同步商品到ES")
    @PostMapping("/sync")
    public ApiResult<String> sync() {
        searchService.fullSync();
        return ApiResult.ok("Sync completed");
    }
}
