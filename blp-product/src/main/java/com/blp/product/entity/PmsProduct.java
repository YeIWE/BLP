package com.blp.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("pms_product")
public class PmsProduct {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long categoryId;
    private Long brandId;
    private String description;
    private String mainImage;
    private String images;
    private BigDecimal price;
    private Integer stock;
    private String unit;
    private Integer status;
    private Integer sort;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
