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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static java.lang.System.currentTimeMillis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidService {
    @Qualifier("redisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    private final ItemRepository itemRepository;
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

//    @Scheduled(fixedDelay = 36000*1000)
//    @Transactional
//    public void checkTime(){
//        ScanOptions options = ScanOptions.scanOptions().match("*").count(100).build();
//
//        ListOperations<String, Object> list = redisTemplate.opsForList();
////        Set<String> keys = redisTemplate.keys("*");
//        Cursor<String> keys = redisTemplate.scan(options);
//        List<String> timeList = new ArrayList<>();
//
////        if (keys != null) {
////            for(String key : keys){
////                int index = key.indexOf(":");
////                long bidIds = Long.valueOf(key.substring(index + 1));
////                System.out.println("bidIds = " + bidIds);
////                Object data = list.index(String.valueOf(bidIds), -1);
////                System.out.println("data = " + data);
////                bid = getInstance().fromJson((String) data, Bid.class);
////                timeList.add(bid.getBidTime());
////            }
////            for(int i = 0; timeList.size() >= i; i++){
////                long bidTime = Long.parseLong(timeList.get(i));
////                if (bidTime <= currentTimeMillis()){
////                    Item item = itemRepository.findById(Long.valueOf(timeList.get(i))).get();
////                    item.setIsSoldout(true);
////                    redisTemplate.delete(timeList.get(i));
////                }
////
////            }
////        }
//        while (keys.hasNext()){
//            long bidIds = extractBidId(keys);
//            System.out.println("bidIds = " + bidIds);
//            Object data = list.index(String.valueOf(bidIds), -1);
//            System.out.println("data = " + data);
//            bid = getInstance().fromJson((String) data, Bid.class);
//            timeList.add(bid.getBidTime());
//        }
//
//    }
//    private Long extractBidId(Cursor<String> keys){
//        String key = new String(keys.next());
//        int index = key.indexOf(":");
//        return Long.valueOf(key.substring(index + 1));
//    }
}
