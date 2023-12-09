package com.fp.backend.chat.service;

import com.fp.backend.chat.domain.Chat;
import com.fp.backend.chat.domain.Message;
import com.fp.backend.chat.dto.ChatDataDTO;
import com.fp.backend.chat.dto.ChatDetailInfoDTO;
import com.fp.backend.chat.dto.ChatPreviewInfoDTO;

import java.util.List;

public interface ChatService {
    List<Long> getIds(String userId);

    List<ChatPreviewInfoDTO> getChatPreviewInfos(List<Long> chatIds, String userId);

    default Chat dtoToEntity(ChatDataDTO dto) {
        return null;
    }

    ChatDetailInfoDTO getChatDetailInfo(Long chatId, String userId);

    List<Message> getChatMessages(Long chatId);
}
