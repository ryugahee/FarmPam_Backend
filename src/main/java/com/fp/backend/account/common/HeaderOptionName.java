package com.fp.backend.account.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HeaderOptionName {

    ROLE("roles"), ACCESSTOKEN("accessToken"), REFRESHTOKEN("refreshToken");

    private final String key;
}
