package com.blp.promotion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("prom_coupon")
public class PromCoupon {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private BigDecimal discount;
    private BigDecimal minAmount;
    private Integer count;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
}
