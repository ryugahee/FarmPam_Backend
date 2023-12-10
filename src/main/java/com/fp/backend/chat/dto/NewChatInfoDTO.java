package com.fp.backend.chat.dto;

import lombok.Data;

@Data
public class NewChatInfoDTO {
    private String firstUserId;
    private String secondUserId;
    private String createdAt;
    private Long itemId;
}
