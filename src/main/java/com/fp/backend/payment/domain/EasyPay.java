package com.fp.backend.payment.domain;

import lombok.*;

@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class EasyPay {
    private String provider;
    private Long amount;
    private Long discountAmount;
}
