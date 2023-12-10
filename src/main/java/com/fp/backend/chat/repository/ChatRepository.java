package com.fp.backend.chat.repository;

import com.fp.backend.chat.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, Long> {
    @Query("{ $or: [ { 'firstUserId': ?0 }, { 'secondUserId': ?0 } ] }")
    List<Chat> findChatsByFirstUserIdOrSecondUserId(String userId);
}
