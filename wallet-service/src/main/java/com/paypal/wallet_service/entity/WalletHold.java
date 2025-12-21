package com.paypal.wallet_service.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_holds")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletHold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(nullable = false)
    private String holdReference; // unique ID for each hold

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE,CANCELLED,CAPTURED

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime expiresAt = LocalDateTime.now();
}
