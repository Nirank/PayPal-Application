package com.paypal.user_service.dto;

import lombok.Data;

@Data
public class SignInRequest {
    private String name;
    private String email;
    private String password;
    private String adminKey;
}
