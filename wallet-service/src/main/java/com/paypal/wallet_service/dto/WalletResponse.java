package com.paypal.wallet_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse {

    private Long id;

    private Long userId;

    private Long balance ;

    private String currency;

    private Long availableBalance;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public WalletResponse(Long id, Long userId, String currency,Long balance, Long availableBalance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.currency = currency;
        this.availableBalance = availableBalance;
    }
}
