package com.fp.backend.chat.dto;

import lombok.Data;

@Data
public class ChatDataDTO {

    private String fromName;

    private String message;

    private Long itemId;
}
