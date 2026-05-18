package com.blp.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blp.common.result.ApiResult;
import com.blp.common.result.PageResult;
import com.blp.product.entity.PmsBrand;
import com.blp.product.service.PmsBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "品牌管理")
@RestController
@RequestMapping("/product/brand")
@RequiredArgsConstructor
public class PmsBrandController {

    private final PmsBrandService brandService;

    @Operation(summary = "品牌分页")
    @GetMapping("/page")
    public ApiResult<PageResult<PmsBrand>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<PmsBrand> result = brandService.page(new Page<>(page, size));
        return ApiResult.ok(PageResult.of(result.getTotal(), page, size, result.getRecords()));
    }

    @Operation(summary = "新增品牌")
    @PostMapping
    public ApiResult<Void> create(@RequestBody PmsBrand brand) {
        brandService.save(brand);
        return ApiResult.ok();
    }

    @Operation(summary = "更新品牌")
    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody PmsBrand brand) {
        brand.setId(id);
        brandService.updateById(brand);
        return ApiResult.ok();
    }

    @Operation(summary = "删除品牌")
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        brandService.removeById(id);
        return ApiResult.ok();
    }
}
