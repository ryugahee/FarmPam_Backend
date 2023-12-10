package com.fp.backend.chat.domain;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    private List<Message> messages = new ArrayList<>();

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

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
