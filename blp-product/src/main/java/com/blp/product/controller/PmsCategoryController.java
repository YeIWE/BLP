package com.blp.product.controller;

import com.blp.common.result.ApiResult;
import com.blp.product.entity.PmsCategory;
import com.blp.product.service.PmsCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "分类管理")
@RestController
@RequestMapping("/product/category")
@RequiredArgsConstructor
public class PmsCategoryController {

    private final PmsCategoryService categoryService;

    @Operation(summary = "分类树")
    @GetMapping("/tree")
    public ApiResult<List<PmsCategory>> tree() {
        return ApiResult.ok(categoryService.getTree());
    }

    @Operation(summary = "新增分类")
    @PostMapping
    public ApiResult<Void> create(@RequestBody PmsCategory category) {
        categoryService.save(category);
        return ApiResult.ok();
    }

    @Operation(summary = "更新分类")
    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody PmsCategory category) {
        category.setId(id);
        categoryService.updateById(category);
        return ApiResult.ok();
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return ApiResult.ok();
    }
}
