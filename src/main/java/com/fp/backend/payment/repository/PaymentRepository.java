package com.fp.backend.payment.repository;

import com.fp.backend.account.entity.Users;
import com.fp.backend.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByUsers(Users user);
}
