package com.fp.backend.payment.service;

import com.fp.backend.account.entity.Users;
import com.fp.backend.account.repository.UserRepository;
import com.fp.backend.payment.domain.entity.Payment;
import com.fp.backend.payment.dto.PaymentInfoDto;
import com.fp.backend.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    @Override
    public Long success(PaymentInfoDto paymentInfoDto) {

        log.info("결제 성공");

        Users user = userRepository.findById(paymentInfoDto.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않은 사용자입니다."));

        Payment payment = toEntity(user, paymentInfoDto.getPaymentInfo());

        paymentRepository.save(payment);
        user.updateFarmMoney(payment.getTotalAmount());

        return user.getFarmMoney();
    }
}
