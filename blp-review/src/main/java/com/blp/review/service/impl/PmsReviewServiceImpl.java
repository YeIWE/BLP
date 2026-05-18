package com.blp.review.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blp.review.entity.PmsReview;
import com.blp.review.mapper.PmsReviewMapper;
import com.blp.review.service.PmsReviewService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PmsReviewServiceImpl extends ServiceImpl<PmsReviewMapper, PmsReview> implements PmsReviewService {

    @Override
    public IPage<PmsReview> pageByProduct(int page, int size, Long productId) {
        LambdaQueryWrapper<PmsReview> wrapper = new LambdaQueryWrapper<>();
        if (productId != null) {
            wrapper.eq(PmsReview::getProductId, productId);
        }
        wrapper.orderByDesc(PmsReview::getCreateTime);
        return page(new Page<>(page, size), wrapper);
    }

    @Override
    public Map<String, Object> stats(Long productId) {
        LambdaQueryWrapper<PmsReview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsReview::getProductId, productId);
        List<PmsReview> reviews = list(wrapper);
        int count = reviews.size();
        double avgRating = 0.0;
        if (count > 0) {
            avgRating = reviews.stream()
                    .mapToInt(PmsReview::getRating)
                    .average()
                    .orElse(0.0);
        }
        BigDecimal rounded = BigDecimal.valueOf(avgRating).setScale(1, RoundingMode.HALF_UP);
        Map<String, Object> result = new HashMap<>();
        result.put("avgRating", rounded.doubleValue());
        result.put("count", count);
        return result;
    }
}
