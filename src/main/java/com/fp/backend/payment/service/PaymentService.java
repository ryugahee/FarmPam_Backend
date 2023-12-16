package com.fp.backend.payment.service;

import com.fp.backend.account.entity.Users;
import com.fp.backend.payment.domain.entity.Payment;
import com.fp.backend.payment.domain.PaymentInfo;
import com.fp.backend.payment.dto.PaymentDto;
import com.fp.backend.payment.dto.PaymentInfoDto;

import java.util.List;

public interface PaymentService {

    Long success(PaymentInfoDto paymentInfoDto);

    List<PaymentDto> getPayments(String username);

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

    default PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .method(payment.getMethod())
                .approvedAt(payment.getApprovedAt())
                .totalAmount(payment.getTotalAmount())
                .provider(payment.getProvider())
                .build();
    }
}
