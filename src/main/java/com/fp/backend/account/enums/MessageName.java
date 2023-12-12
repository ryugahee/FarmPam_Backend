package com.fp.backend.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageName {
    REQUIRE_REFRESH_TOKEN("please send refreshToken"), RE_LOGIN("login again");

    private final String key;
}
