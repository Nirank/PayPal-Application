package com.paypal.wallet_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitRequest {
    private Long amount;
    private Long userId;

    private Long walletId;
    private String currency;
    private String ref;
}
