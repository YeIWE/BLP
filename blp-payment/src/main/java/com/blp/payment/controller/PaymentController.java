package com.blp.payment.controller;

import com.blp.common.result.ApiResult;
import com.blp.payment.entity.PayPayment;
import com.blp.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "支付管理")
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "支付")
    @PostMapping("/pay")
    public ApiResult<PayPayment> pay(@RequestBody Map<String, Object> body) {
        Long orderId = Long.valueOf(body.get("orderId").toString());
        String orderNo = (String) body.get("orderNo");
        java.math.BigDecimal amount = new java.math.BigDecimal(body.get("amount").toString());
        String payMethod = (String) body.getOrDefault("payMethod", "ALIPAY");
        PayPayment payment = paymentService.pay(orderId, orderNo, amount, payMethod);
        return ApiResult.ok(payment);
    }

    @Operation(summary = "支付详情")
    @GetMapping("/{id}")
    public ApiResult<PayPayment> detail(@PathVariable Long id) {
        return ApiResult.ok(paymentService.getPaymentDetail(id));
    }
}
