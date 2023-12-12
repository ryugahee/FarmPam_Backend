package com.fp.backend.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HeaderOptionName {

    ROLE("roles"), ACCESSTOKEN("accessToken"), REFRESHTOKEN("refreshToken");

    private final String key;
}
