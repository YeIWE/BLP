package com.blp.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blp.common.result.ApiResult;
import com.blp.common.result.PageResult;
import com.blp.product.entity.PmsProduct;
import com.blp.product.service.PmsProductService;
import com.blp.product.util.MinioUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "商品管理")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class PmsProductController {

    private final PmsProductService productService;
    private final MinioUtil minioUtil;

    @Operation(summary = "分页查询商品")
    @GetMapping("/page")
    public ApiResult<PageResult<PmsProduct>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        IPage<PmsProduct> result = productService.pageProducts(page, size, categoryId, keyword);
        return ApiResult.ok(PageResult.of(result.getTotal(), page, size, result.getRecords()));
    }

    @Operation(summary = "商品详情")
    @GetMapping("/{id}")
    public ApiResult<PmsProduct> detail(@PathVariable Long id) {
        return ApiResult.ok(productService.getById(id));
    }

    @Operation(summary = "新增商品")
    @PostMapping
    public ApiResult<Void> create(@RequestBody PmsProduct product) {
        productService.save(product);
        return ApiResult.ok();
    }

    @Operation(summary = "更新商品")
    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody PmsProduct product) {
        product.setId(id);
        productService.updateById(product);
        return ApiResult.ok();
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        productService.removeById(id);
        return ApiResult.ok();
    }

    @Operation(summary = "上传图片")
    @PostMapping("/upload")
    public ApiResult<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        return ApiResult.ok(minioUtil.upload(file));
    }
}
