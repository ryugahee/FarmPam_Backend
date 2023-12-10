package com.fp.backend.chat.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestChatPreviewInfoDTO {
    private List<Long> chatIds;
    private String userId;
}
