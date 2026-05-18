package com.blp.cart.service;

import com.blp.cart.entity.CartItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final String CART_KEY_PREFIX = "cart:user:";
    private static final long CART_TTL_DAYS = 30;

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private String cartKey(Long userId) {
        return CART_KEY_PREFIX + userId;
    }

    public List<CartItem> listCart(Long userId) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cartKey(userId));
        return entries.values().stream()
            .map(obj -> objectMapper.convertValue(obj, CartItem.class))
            .collect(Collectors.toList());
    }

    public void addItem(Long userId, CartItem item) {
        item.setUserId(userId);
        String key = cartKey(userId);
        String field = String.valueOf(item.getProductId());
        CartItem existing = getItem(userId, item.getProductId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
            redisTemplate.opsForHash().put(key, field, existing);
        } else {
            redisTemplate.opsForHash().put(key, field, item);
        }
        redisTemplate.expire(key, CART_TTL_DAYS, TimeUnit.DAYS);
    }

    public void updateQuantity(Long userId, Long productId, Integer quantity) {
        CartItem item = getItem(userId, productId);
        if (item == null) {
            throw new RuntimeException("购物车商品不存在");
        }
        item.setQuantity(quantity);
        redisTemplate.opsForHash().put(cartKey(userId), String.valueOf(productId), item);
    }

    public void removeItem(Long userId, Long productId) {
        redisTemplate.opsForHash().delete(cartKey(userId), String.valueOf(productId));
    }

    public void clearCart(Long userId) {
        redisTemplate.delete(cartKey(userId));
    }

    private CartItem getItem(Long userId, Long productId) {
        Object obj = redisTemplate.opsForHash().get(cartKey(userId), String.valueOf(productId));
        if (obj == null) return null;
        return objectMapper.convertValue(obj, CartItem.class);
    }
}
