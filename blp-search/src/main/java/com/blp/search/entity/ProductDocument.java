package com.blp.search.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDocument {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName;
    private String brandName;
    private BigDecimal price;
    private String description;
    private LocalDateTime createTime;
}
