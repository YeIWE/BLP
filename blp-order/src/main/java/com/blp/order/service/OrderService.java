package com.blp.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blp.common.exception.BizException;
import com.blp.order.entity.OmsOrder;
import com.blp.order.entity.OmsOrderItem;
import com.blp.order.mapper.OmsOrderItemMapper;
import com.blp.order.mapper.OmsOrderMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService extends ServiceImpl<OmsOrderMapper, OmsOrder> {

    private final OmsOrderItemMapper orderItemMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    private static final String CART_KEY_PREFIX = "cart:user:";

    @Transactional
    public OmsOrder createOrder(Long userId, String receiverName, String receiverPhone, String receiverAddress) {
        // Read cart items from Redis
        String cartKey = CART_KEY_PREFIX + userId;
        Map<Object, Object> cartEntries = redisTemplate.opsForHash().entries(cartKey);
        if (cartEntries.isEmpty()) {
            throw new BizException("购物车为空");
        }

        List<Map<String, Object>> cartItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Object value : cartEntries.values()) {
            Map<String, Object> item = objectMapper.convertValue(value, new TypeReference<Map<String, Object>>() {});
            cartItems.add(item);
            BigDecimal price = new BigDecimal(item.get("price").toString());
            int qty = Integer.parseInt(item.get("quantity").toString());
            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(qty)));
        }

        // Create order
        OmsOrder order = new OmsOrder();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        save(order);

        // Create order items
        for (Map<String, Object> item : cartItems) {
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(Long.valueOf(item.get("productId").toString()));
            orderItem.setProductName((String) item.get("productName"));
            orderItem.setProductImage((String) item.get("productImage"));
            orderItem.setPrice(new BigDecimal(item.get("price").toString()));
            orderItem.setQuantity(Integer.parseInt(item.get("quantity").toString()));
            orderItemMapper.insert(orderItem);
        }

        // Clear cart
        redisTemplate.delete(cartKey);

        // Send order event
        Map<String, Object> event = new HashMap<>();
        event.put("type", "ORDER_CREATED");
        event.put("orderId", order.getId());
        event.put("orderNo", order.getOrderNo());
        event.put("userId", userId);
        event.put("totalAmount", totalAmount);
        rabbitTemplate.convertAndSend("order.events", event);

        return order;
    }

    public IPage<OmsOrder> pageOrders(int page, int size, Long userId, String status) {
        LambdaQueryWrapper<OmsOrder> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(OmsOrder::getUserId, userId);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(OmsOrder::getStatus, status);
        }
        wrapper.orderByDesc(OmsOrder::getCreateTime);
        return page(new Page<>(page, size), wrapper);
    }

    public OmsOrder getOrderDetail(Long id) {
        OmsOrder order = getById(id);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        return order;
    }

    public void updateStatus(Long id, String status) {
        OmsOrder order = getById(id);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        order.setStatus(status);
        updateById(order);

        // Send status change event
        Map<String, Object> event = new HashMap<>();
        event.put("type", "ORDER_STATUS_CHANGED");
        event.put("orderId", id);
        event.put("orderNo", order.getOrderNo());
        event.put("status", status);
        rabbitTemplate.convertAndSend("order.events", event);
    }

    public List<OmsOrderItem> getOrderItems(Long orderId) {
        LambdaQueryWrapper<OmsOrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OmsOrderItem::getOrderId, orderId);
        return orderItemMapper.selectList(wrapper);
    }

    private String generateOrderNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
            + String.format("%06d", new Random().nextInt(999999));
    }
}
