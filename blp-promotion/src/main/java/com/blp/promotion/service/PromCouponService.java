package com.blp.promotion.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blp.promotion.entity.PromCoupon;

public interface PromCouponService extends IService<PromCoupon> {
    IPage<PromCoupon> pageCoupons(int page, int size);
}
