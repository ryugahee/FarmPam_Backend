package com.fp.backend.auction.bid.service;

import com.fp.backend.auction.bid.dto.Bid;
import com.fp.backend.auction.bid.dto.BidData;
import com.fp.backend.auction.entity.Item;
import com.fp.backend.auction.repository.ItemRepository;
import com.fp.backend.system.config.websocket.SocketVO;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Qualifier("redisTemplate_Bid")
    private final RedisTemplate<String, Object> redisTemplate_Bid;
    private final ItemRepository itemRepository;
    private static Gson gson;
    private static Bid bid;
    public static synchronized  Gson getInstance(){
        if(gson == null) gson = new Gson();
        return gson;
    }

    public void setValuesPush(String key, Object data){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
        bid = getInstance().fromJson((String) data, Bid.class);
        int bidPrice = Integer.parseInt(bid.getBidPrice());
        Object currentObject = list.index(key, 0);
        bid = getInstance().fromJson((String) currentObject, Bid.class);

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
            bid = getInstance().fromJson((String) data, Bid.class);
            SocketVO socketVO = new SocketVO(key, bid.getBidPrice());
            stringList.add(socketVO);
        }
        return stringList;
    }
    public void setBidRegistryInfo(String key, String userName, String minPrice, long endTime){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
        BidData bidData = new BidData(userName, minPrice, endTime);
        String data = getInstance().toJson(bidData);
        list.leftPush(key, data);
    }

    public Bid currentPrice(String key){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
        Object currentObject = list.index(key, 0);
        bid = getInstance().fromJson((String) currentObject, Bid.class);
        Bid currentBid = new Bid();
        currentBid.setBidId(key);
        currentBid.setBidPrice(bid.getBidPrice());
        return currentBid;
    }
    public List<Bid> currentBid(){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
        List<Bid> allCurrentBid = new ArrayList<>();
        Set<String> keys = redisTemplate_Bid.keys("*");
        if (keys != null) {
            for (String key : keys) {
                int index = key.indexOf(":");
                long bidIds = Long.valueOf(key.substring(index + 1));
                Object data = list.index(String.valueOf(bidIds), 0);
                bid = getInstance().fromJson((String) data, Bid.class);
                Bid currentBid = new Bid();
                currentBid.setBidId(key);
                currentBid.setBidPrice(bid.getBidPrice());
                allCurrentBid.add(currentBid);
            }
        }

        return allCurrentBid;
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void insertSeasonScheduler() {

    }
    @Scheduled(fixedDelay = 10*1000)
    @Transactional
    @Async
    public void checkTime(){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
        //모든 Keys * 값 호출
        Set<String> keys = redisTemplate_Bid.keys("*");
        if (keys != null) {

            for(String key : keys){
                int index = key.indexOf(":");
                long bidIds = Long.valueOf(key.substring(index + 1));
                //bidIds에 해당하는 가장 처음 인덱스(경매 종료 예정 시간이 저장된 데이터) 조회
                Object data = list.index(String.valueOf(bidIds), -1);
                //Bid 객체에 파싱된 데이터를 저장
                bid = getInstance().fromJson((String) data, Bid.class);
                long bidTime = Long.parseLong(bid.getBidTime());
                // long형태로 findById로 itemTable 조회
                Item item = itemRepository.findById(bidIds)
                        .orElseThrow(IllegalAccessError::new);

                //현재 시간이 경매 종료 예정 시간 이상일 때
                if(bidTime <= currentTimeMillis()){
                    item.setIsSoldout(true);
                    item.setLastBidPrice(Integer.parseInt(bid.getBidPrice()));
                    itemRepository.save(item);
                    redisTemplate_Bid.delete(String.valueOf(bidIds));
                }else {
                    item.setCurrentBidPrice(Integer.parseInt(bid.getBidPrice()));
                    itemRepository.save(item);
                }


            }
        }
  }
}
