package com.fp.backend.payment.service;

import com.fp.backend.account.entity.Users;
import com.fp.backend.payment.domain.entity.Payment;
import com.fp.backend.payment.domain.PaymentInfo;
import com.fp.backend.payment.dto.PaymentInfoDto;

public interface PaymentService {

    Long success(PaymentInfoDto paymentInfoDto);

    default Payment toEntity(Users user, PaymentInfo paymentInfo) {
        return Payment.builder()
                .method(paymentInfo.getMethod())
                .totalAmount(paymentInfo.getTotalAmount())
                .requestedAt(paymentInfo.getRequestedAt())
                .approvedAt(paymentInfo.getApprovedAt())
                .orderName(paymentInfo.getOrderName())
                .orderId(paymentInfo.getOrderId())
                .paymentKey(paymentInfo.getPaymentKey())
                .provider(paymentInfo.getEasyPay().getProvider())
                .failureMessage(paymentInfo.getPayFailure() == null ? null : paymentInfo.getPayFailure().getMessage())
                .users(user)
                .build();
    }
}
