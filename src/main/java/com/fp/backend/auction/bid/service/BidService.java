package com.fp.backend.auction.bid.service;

import com.fp.backend.account.entity.Users;
import com.fp.backend.account.repository.UserRepository;
import com.fp.backend.auction.bid.dto.Bid;
import com.fp.backend.auction.bid.dto.BidData;
import com.fp.backend.auction.bid.dto.BidVO;
import com.fp.backend.auction.entity.Item;
import com.fp.backend.auction.repository.ItemRepository;
import com.fp.backend.farmmoney.dto.SuccessfulBidDto;
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
    private final UserRepository userRepository;
    private static Gson gson;
    private static Bid bid;
    public static synchronized  Gson getInstance(){
        if(gson == null) gson = new Gson();
        return gson;
    }

    public void setValuesPush(String key, Object data){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();


        bid = getInstance().fromJson((String) data, Bid.class);
        long bidPrice = Long.parseLong(bid.getBidPrice());
        String userName = bid.getUserName();
        Users users = userRepository.findById(userName).orElseThrow(() -> new RuntimeException("사용자가 없습니다!"));
        long farmMoney = users.getFarmMoney();

        Object currentObject = list.index(key, 0);
        bid = getInstance().fromJson((String) currentObject, Bid.class);

        long current = Long.parseLong(bid.getBidPrice());
        if (current < bidPrice && farmMoney >= bidPrice){
            System.out.println(bidPrice);
            list.leftPush(key, data);

        }
    }
    @Transactional(readOnly = true)
    public List<BidVO> getMyBidPrice(String key, String userName){
        List<BidVO> allBidList = getValuesListAll(key);
        System.out.println("여기 존나존나 여기: " + allBidList.get(0));
        List<BidVO> myBidList = new ArrayList<>();
        for(int i = 0; allBidList.size() > i; i++){
            BidVO bidVO = allBidList.get(i);
            System.out.println("bid bid존나게 존나 여기: "+ bidVO.getContent());
            String BidListName = bidVO.getUserName();
            System.out.println("리스트 네임BidListName = " + BidListName);
            System.out.println("존나 입풋 네임"+userName);
            if(BidListName.equals(userName)){
                myBidList.add(bidVO);
            }
        }
        return myBidList;
    }

    public String getValuesLastIndex(String key){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
        return (String) list.index(key, 0);
    }


    @Transactional(readOnly = true)
    public List<BidVO> getValuesListAll(String key){
        ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
        List<BidVO> stringList = new ArrayList<>();
        List<Object> bidList = list.range(key, 0, -1);
        for(int i = bidList.size()-1; i >= 0; i--){
            Object data = list.index(key, i);
            bid = getInstance().fromJson((String) data, Bid.class);
            String bidUserName = bid.getUserName();
            String bidPrice = bid.getBidPrice();

            BidVO bidVO = new BidVO(key, bidUserName, bidPrice);
            stringList.add(bidVO);
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
        Object currentObject = list.index(key, -1);
        bid = getInstance().fromJson((String) currentObject, Bid.class);
        Bid currentBid = new Bid();
        currentBid.setBidId(key);
        currentBid.setBidPrice(bid.getBidPrice());
        System.out.println("bid.getBidPrice() = " + bid.getBidPrice());
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

//    public Object myBidPrice(String key, Object data){
//
//    }

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

        System.out.println("======================================");
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
                    SuccessfulBidDto successfulBidDto = successfulBid(String.valueOf(bidIds));
                    String lastUser = successfulBidDto.getUsername();
                    Users users = userRepository.findById(lastUser).orElseThrow(() -> new RuntimeException("사용자가 없습니다!"));
                    users.payFarmMoney(successfulBidDto.getAmount());
                    item.setIsSoldout(true);
                    System.out.println("user==================================s = " + item.getIsSoldout());
                    item.setLastBidPrice(Integer.parseInt(bid.getBidPrice()));
                    item.setBuyer(bid.getUserName());
                    itemRepository.save(item);


                    redisTemplate_Bid.delete(String.valueOf(bidIds));
                }else {
                    item.setCurrentBidPrice(Integer.parseInt(bid.getBidPrice()));
                    itemRepository.save(item);
                }
            }
        }
  }
  public SuccessfulBidDto successfulBid(String key){
      ListOperations<String, Object> list = redisTemplate_Bid.opsForList();
      Object data = list.index(key, 0);
      bid= getInstance().fromJson((String) data, Bid.class);
      SuccessfulBidDto successfulBidDto = new SuccessfulBidDto();
      successfulBidDto.setUsername(bid.getUserName());
      successfulBidDto.setAmount(Long.valueOf(bid.getBidPrice()));


      return successfulBidDto;
  }

}
