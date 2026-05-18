package com.blp.payment.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blp.common.exception.BizException;
import com.blp.payment.entity.PayPayment;
import com.blp.payment.mapper.PayPaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService extends ServiceImpl<PayPaymentMapper, PayPayment> {

    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public PayPayment pay(Long orderId, String orderNo, BigDecimal amount, String payMethod) {
        // Check if already paid
        PayPayment existing = lambdaQuery()
            .eq(PayPayment::getOrderId, orderId)
            .eq(PayPayment::getStatus, "SUCCESS")
            .one();
        if (existing != null) {
            throw new BizException("该订单已支付");
        }

        // Create payment record
        PayPayment payment = new PayPayment();
        payment.setOrderId(orderId);
        payment.setOrderNo(orderNo);
        payment.setAmount(amount);
        payment.setPayMethod(payMethod != null ? payMethod : "ALIPAY");
        payment.setStatus("SUCCESS");  // Mock: always success
        payment.setTradeNo(generateTradeNo());
        payment.setPayTime(LocalDateTime.now());
        save(payment);

        // Send payment success event to update order status
        Map<String, Object> event = new HashMap<>();
        event.put("type", "PAYMENT_SUCCESS");
        event.put("orderId", orderId);
        event.put("orderNo", orderNo);
        event.put("paymentId", payment.getId());
        event.put("tradeNo", payment.getTradeNo());
        rabbitTemplate.convertAndSend("order.events", event);

        return payment;
    }

    public PayPayment getPaymentDetail(Long id) {
        PayPayment payment = getById(id);
        if (payment == null) {
            throw new BizException("支付记录不存在");
        }
        return payment;
    }

    private String generateTradeNo() {
        return "TRD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
            + String.format("%04d", new Random().nextInt(9999));
    }
}
