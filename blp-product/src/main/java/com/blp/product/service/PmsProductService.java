package com.blp.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blp.product.entity.PmsProduct;
import com.blp.product.mapper.PmsProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PmsProductService extends ServiceImpl<PmsProductMapper, PmsProduct> {

    public IPage<PmsProduct> pageProducts(int page, int size, Long categoryId, String keyword) {
        LambdaQueryWrapper<PmsProduct> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null && categoryId > 0) {
            wrapper.eq(PmsProduct::getCategoryId, categoryId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(PmsProduct::getName, keyword);
        }
        wrapper.eq(PmsProduct::getStatus, 1).orderByDesc(PmsProduct::getCreateTime);
        return page(new Page<>(page, size), wrapper);
    }
}
