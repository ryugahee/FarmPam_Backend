package com.fp.backend.chat.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@Document(collection = "chats")
public class Chat {

    @Transient
    public static final String SEQUENCE_NAME = "chats_sequence";

    @Id
    private Long chatId;

    private String firstUserId;

    private String secondUserId;

    private List<ChatMessage> chatMessages;

    private String createdAt;

    private Long itemId;

    @Builder
    public Chat(Long chatId, String firstUserId, String secondUserId, List<ChatMessage> chatMessages, String createdAt, Long itemId) {
        this.chatId = chatId;
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.chatMessages = chatMessages;
        this.createdAt = createdAt;
        this.itemId = itemId;
    }

    public void addMessage(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
    }
}
