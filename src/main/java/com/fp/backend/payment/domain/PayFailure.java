package com.fp.backend.payment.domain;

import lombok.*;

@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PayFailure {

    // 에러 코드
    private String code;

    // 에러 메시지
    private String message;
}
