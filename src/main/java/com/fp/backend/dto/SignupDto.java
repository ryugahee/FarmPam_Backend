package com.fp.backend.dto;

import lombok.Getter;

@Getter
public class SignupDto {

    private String username;
    private String password;
    private String email;
    private int age;
    private String city;
}
