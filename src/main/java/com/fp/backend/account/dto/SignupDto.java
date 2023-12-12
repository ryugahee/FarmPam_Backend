package com.fp.backend.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {

    private String username;
    private String password;
    private boolean enabled;

    private String realName;

    private String nickname;

    private Integer age;

    private String email;

    private String mailCode;

    private String streetAddress;

    private String detailAddress;

    public SignupDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}

