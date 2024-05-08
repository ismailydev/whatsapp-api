package com.example.whatsapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationReq {
    private String phone;
    private String code;
}
