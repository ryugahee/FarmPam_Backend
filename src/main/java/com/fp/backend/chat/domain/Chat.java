package com.fp.backend.chat.domain;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@Document(collection = "chats")
public class Chat {

    @Transient
    private static final String SEQUENCE_NAME = "chats_sequence";

    @Id
    private Long chatId;

    private String firstUserId;

    private String secondUserId;

    private List<Message> messages;

    @Column(updatable = false)
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private String createdAt;

    private Long itemId;
}
