package com.fp.backend.auction.repository;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> bidRoomMap;
    public static final String ENTER_INFO = "ENTER_INFO";

    public void mappingBidRoom(String sessionId, String roomId){
        bidRoomMap.put(ENTER_INFO, sessionId, roomId);
    }

    public String getUserEnterRoomId(String sessionId) {
        return bidRoomMap.get(ENTER_INFO, sessionId);
    }

    public void removeUserEnterInfo(String sessionId) {
        bidRoomMap.delete(ENTER_INFO, sessionId);
    }
}
