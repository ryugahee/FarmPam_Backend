package com.fp.backend.chat.dto;

import lombok.Data;

@Data
public class MessageDTO {

    private String fromUserId;

    private String message;

    private String updateAt;
}
