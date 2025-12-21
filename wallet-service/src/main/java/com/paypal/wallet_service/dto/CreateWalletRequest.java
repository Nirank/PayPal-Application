package com.paypal.wallet_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletRequest {
    private Long userId;
    private String currency;
    private Long initialBalance;
}
