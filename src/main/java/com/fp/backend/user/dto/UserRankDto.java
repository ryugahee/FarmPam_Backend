package com.fp.backend.user.dto;

import lombok.Getter;

@Getter
public class UserRankDto {

    private String userName;

    private String nickname;

    private String imageUrl;

    private long count;

    public UserRankDto(String userName, String nickname, String imageUrl, long count) {
        this.userName = userName;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.count = count;
    }

}
