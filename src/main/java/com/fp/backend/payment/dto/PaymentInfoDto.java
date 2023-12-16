package com.fp.backend.payment.dto;

import com.fp.backend.payment.domain.PaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInfoDto {
    private String username;
    private PaymentInfo paymentInfo;
}
