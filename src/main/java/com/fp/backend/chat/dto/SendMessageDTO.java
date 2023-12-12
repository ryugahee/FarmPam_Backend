package com.fp.backend.chat.dto;

import com.fp.backend.chat.domain.ChatMessage;
import lombok.Data;

@Data
public class SendMessageDTO {
    private ChatMessage chatMessage;
    private Long chatId;
}
