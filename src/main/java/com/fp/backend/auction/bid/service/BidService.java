package com.fp.backend.auction.bid.service;

import com.fp.backend.auction.bid.dto.Bid;
import com.fp.backend.auction.entity.Item;
import com.fp.backend.auction.repository.ItemRepository;
import com.fp.backend.system.config.websocket.SocketVO;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;

import static java.lang.System.currentTimeMillis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidService {

    @Qualifier("redisTemplate_Bid")
    private final RedisTemplate<String, Object> redisTemplate_Bid;
    private final ItemRepository itemRepository;
    private static Gson gson;
    private static Bid bid;
    public static synchronized  Gson getInstance(){
        if(gson == null) gson = new Gson();
        return gson;
    }

    public String currentPrice(String key){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
        Object currentObject = list.index(key, 0);
        bid = getInstance().fromJson((String) currentObject, Bid.class);
        return bid.getBidPrice();
    }
    public void setBidPush(String key, String data){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();

        list.leftPush(key, data);
        System.out.println("data = " + data);
    }

    public void setValuesPush(String key, Object data){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
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
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
        return (String) list.index(key, 0);
    }
    @Transactional(readOnly = true)
    public List<SocketVO> getValuesListAll(String key){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
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

    @Scheduled(fixedDelay = 10*1000)
    @Transactional
    public void checkTime(){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
        Set<String> keys = redisTemplate_Bid.keys("*");
        if (keys != null) {
            for(String key : keys){
                int index = key.indexOf(":");
                long bidIds = Long.valueOf(key.substring(index + 1));
                System.out.println("bidIds = " + bidIds);
                Object data = list.index(String.valueOf(bidIds), -1);
                System.out.println("data = " + data);

                System.out.println("nowTime = " + currentTimeMillis());
                bid = getInstance().fromJson((String) data, Bid.class);
                long bidTime = Long.parseLong(bid.getBidTime());
                if(bidTime <= currentTimeMillis()){
                    Item item = itemRepository.findById(bidIds)
                            .orElseThrow(IllegalAccessError::new);
                    item.setIsSoldout(true);
                    itemRepository.save(item);
                    redisTemplate_Bid.delete(String.valueOf(bidIds));
                }

            }
        }
  }
}
