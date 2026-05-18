package com.blp.review.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blp.review.entity.PmsReview;

import java.util.Map;

public interface PmsReviewService extends IService<PmsReview> {
    IPage<PmsReview> pageByProduct(int page, int size, Long productId);
    Map<String, Object> stats(Long productId);
}
