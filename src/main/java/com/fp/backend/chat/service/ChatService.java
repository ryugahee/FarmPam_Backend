package com.fp.backend.chat.service;

import com.fp.backend.chat.domain.Chat;
import com.fp.backend.chat.domain.ChatMessage;
import com.fp.backend.chat.dto.ChatDetailInfoDTO;
import com.fp.backend.chat.dto.ChatPreviewInfoDTO;
import com.fp.backend.chat.dto.NewChatInfoDTO;
import com.fp.backend.chat.dto.SendMessageDTO;

import java.util.List;

public interface ChatService {
    List<Long> getChatIds(String userId);

    List<ChatPreviewInfoDTO> getChatPreviewInfos(List<Long> chatIds, String userId);

    ChatDetailInfoDTO getChatDetailInfo(Long chatId, String userId);

    List<ChatMessage> getChatMessages(Long chatId);

    void sendMessage(SendMessageDTO dto);

    Long createChat(NewChatInfoDTO dto);

    default Chat dtoToEntity(NewChatInfoDTO dto, Long newChatId) {
        return Chat.builder()
                .chatId(newChatId)
                .firstUserId(dto.getFirstUserId())
                .secondUserId(dto.getSecondUserId())
                .createdAt(dto.getCreatedAt())
                .itemId(dto.getItemId())
                .build();
    }
}
