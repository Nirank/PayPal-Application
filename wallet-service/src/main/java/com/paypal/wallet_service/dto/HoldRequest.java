package com.paypal.wallet_service.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoldRequest {
    private Long amount;
    private Long userId;
    private String currency;
}
