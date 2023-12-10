package com.fp.backend.chat.dto;

import com.fp.backend.chat.domain.Message;
import lombok.Data;

@Data
public class SendMessageDTO {
    private Message message;
    private Long chatId;
}
