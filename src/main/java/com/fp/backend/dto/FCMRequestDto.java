package com.fp.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMRequestDto {
    private Long targetUserId;
    private String title;
    private String body;


    @Builder
    public FCMRequestDto(Long targetUserId, String title, String body){

    }
}
