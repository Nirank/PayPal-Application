package com.paypal.wallet_service.dto;


import com.paypal.wallet_service.entity.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoldResponse {

    private Wallet wallet;
    private String holdReference;
    private Long amount;
    private String status;

    public HoldResponse(String holdReference, String status, Long amount) {
        this.holdReference = holdReference;
        this.status = status;
        this.amount = amount;
    }
}
