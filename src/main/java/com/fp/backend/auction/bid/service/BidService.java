package com.fp.backend.auction.bid.service;

import com.fp.backend.auction.bid.dto.Bid;
import com.fp.backend.system.config.websocket.SocketVO;
import com.google.gson.Gson;
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
    private static Gson gson;
    private static Bid bid;
    public static synchronized  Gson getInstance(){
        if(gson == null) gson = new Gson();
        return gson;
    }
    public void setValuesPush(String key, Object data){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        bid = getInstance().fromJson((String) data, Bid.class);
        int bidPrice = Integer.parseInt(bid.getBidPrice());

        System.out.println("bid.getUserName() = " + bid.getUserName());
        System.out.println("bidPrice = " + bidPrice);
        Object currentObject = list.index(key, 0);
        bid = getInstance().fromJson((String) currentObject, Bid.class);
        System.out.println("currentObject = " + bid.getBidPrice());
        int current = Integer.parseInt(bid.getBidPrice());
        if (current < bidPrice){
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
            Object data = list.index(key, i);
            System.out.println("data = " + data);
            bid = getInstance().fromJson((String) data, Bid.class);
            System.out.println("bidData = " + bid.getUserName());
            System.out.println("bid.getBidPrice() = " + bid.getBidPrice());
            SocketVO socketVO = new SocketVO(key, bid.getBidPrice());
            stringList.add(socketVO);
        }

        return stringList;
    }
}
