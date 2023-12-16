package com.fp.backend.payment.service;

import com.fp.backend.account.entity.Users;
import com.fp.backend.account.repository.UserRepository;
import com.fp.backend.payment.domain.entity.Payment;
import com.fp.backend.payment.dto.PaymentDto;
import com.fp.backend.payment.dto.PaymentInfoDto;
import com.fp.backend.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

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

        Users user = getUser(paymentInfoDto.getUsername());

        Payment payment = toEntity(user, paymentInfoDto.getPaymentInfo());

        paymentRepository.save(payment);
        user.updateFarmMoney(payment.getTotalAmount());

        return user.getFarmMoney();
    }

    @Override
    public List<PaymentDto> getPayments(String username) {


        log.info("충전 내역 가져오기");

        Users users = getUser(username);

        List<Payment> payments = paymentRepository.findAllByUsers(users);

        return payments.stream()
                .filter(this::isSuccessPayment)
                .map(this::toDto)
                .sorted(Comparator.comparing(PaymentDto::getApprovedAt).reversed())
                .toList();
    }

    private boolean isSuccessPayment(Payment payment) {
        return payment.getFailureMessage() == null;
    }

    private Users getUser(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자를 찾으려고 합니다."));
    }
}
