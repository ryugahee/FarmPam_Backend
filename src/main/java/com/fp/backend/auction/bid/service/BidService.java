package com.fp.backend.auction.bid.service;

import com.fp.backend.system.config.websocket.SocketVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidService {
    private final RedisTemplate<String, Object> redisTemplate;
    public void setValuesPush(String key, String data){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        Object currentObject = list.index(key, 0);
        int current = Integer.parseInt((String) currentObject);
        System.out.println("currentBidPrice = " + current);
        int receive = Integer.parseInt(data);
        System.out.println("receiveBidPrice = " + receive);
        if (current < receive){
            list.leftPush(key, data);
        }
    }

    public String getValuesLastIndex(String key){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return (String) list.index(key, 0);
    }
    @Transactional(readOnly = true)
    public List<SocketVO> getValuesListAll(String key){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        List<SocketVO> stringList = new ArrayList<>();
        List<Object> bidList = list.range(key, 0, -1);
        for(int i = bidList.size()-1; i >= 0; i--){
            Object bid = list.index(key, i);
            SocketVO socketVO = new SocketVO(key, bid);
            stringList.add(socketVO);
        }

        return stringList;
    }
}
