package com.fp.backend.payment.domain.entity;

import com.fp.backend.account.entity.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long paymentId;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private Long totalAmount;

    @Column(nullable = false)
    private String requestedAt;

    @Column(nullable = false)
    private String approvedAt;

    @Column(nullable = false)
    private String orderName;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String paymentKey;

    @Column
    private String provider;

    @Column
    private String failureMessage;

    @ManyToOne
    @JoinColumn(name = "username")
    private Users users;
}
