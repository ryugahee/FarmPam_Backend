package com.fp.backend.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

    private final List<ChatMessage> chatMessages = new ArrayList<>();

    private String createdAt;

    private Long itemId;

    @Builder
    public Chat(Long chatId, String firstUserId, String secondUserId, String createdAt, Long itemId) {
        this.chatId = chatId;
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.createdAt = createdAt;
        this.itemId = itemId;
    }

    public void addMessage(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
    }
}
