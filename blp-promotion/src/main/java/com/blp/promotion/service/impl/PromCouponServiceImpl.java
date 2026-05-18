package com.blp.promotion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blp.promotion.entity.PromCoupon;
import com.blp.promotion.mapper.PromCouponMapper;
import com.blp.promotion.service.PromCouponService;
import org.springframework.stereotype.Service;

@Service
public class PromCouponServiceImpl extends ServiceImpl<PromCouponMapper, PromCoupon> implements PromCouponService {

    @Override
    public IPage<PromCoupon> pageCoupons(int page, int size) {
        LambdaQueryWrapper<PromCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(PromCoupon::getId);
        return page(new Page<>(page, size), wrapper);
    }
}
