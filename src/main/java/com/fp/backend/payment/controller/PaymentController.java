package com.fp.backend.payment.controller;

import com.fp.backend.payment.dto.PaymentDto;
import com.fp.backend.payment.dto.PaymentInfoDto;
import com.fp.backend.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payment/success")
    public ResponseEntity<Long> successPayment(@RequestBody PaymentInfoDto paymentInfoDto) {
        log.info("{}님의 결제 정보 등록", paymentInfoDto.getUsername());
        log.info("{}", paymentInfoDto);
        return new ResponseEntity<>(paymentService.success(paymentInfoDto), HttpStatus.OK);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDto>> getPayments(@RequestParam String username) {
        log.info("{} 님의 충전 내역", username);
        return new ResponseEntity<>(paymentService.getPayments(username), HttpStatus.OK);
    }
}
