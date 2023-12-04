package com.fp.backend.account.dto;

import lombok.Getter;

@Getter
public class SignupDto {

    private String userName;
    private String userPassword;
    private String userEmail;
    private int userPhoneNumber;
    private String userLocal;
}
