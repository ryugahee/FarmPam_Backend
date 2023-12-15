package com.fp.backend.payment.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PaymentInfo {

    // 결제타입
    @NotNull
    private String method;

    // 충전금액
    @NotNull
    private Long totalAmount;

    // 결제요청일자
    @NotNull
    private String requestedAt;

    // 결제승인일자
    @NotNull
    private String approvedAt;

    // 주문명
    @NotNull
    private String orderName;

    // 주문번호
    @NotNull
    private String orderId;

    // 결제 키
    @NotNull
    private String paymentKey;

    // 간편결제 정보
    private EasyPay easyPay;

    // 결제 실패
    private PayFailure payFailure;
}
