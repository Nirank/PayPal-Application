package com.paypal.transaction_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoldResponse {

    private String holdReference;
    private Long amount;
    private String status;

    public HoldResponse(String holdReference, String status, Long amount) {
        this.holdReference = holdReference;
        this.status = status;
        this.amount = amount;
    }
}
