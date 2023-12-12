package com.fp.backend.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiName {

    LOGIN("/api/login"), SIGNUP("/api/user/signup"), LOGOUT("/api/userLogout");

    private final String key;
}
