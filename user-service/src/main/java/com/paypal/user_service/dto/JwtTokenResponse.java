package com.paypal.user_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class JwtTokenResponse {

    private String token;
}
