package com.blp.cart.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItem {
    private Long productId;
    private Long userId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private Integer quantity;
}
