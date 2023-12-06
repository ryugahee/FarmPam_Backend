package com.fp.backend.auction.bid.service;


import com.fp.backend.auction.bid.dto.Bid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BidService {
    private Map<String, Bid> bids;

    @PostConstruct
    private void init(){
        bids = new LinkedHashMap<>();
    }

    public List<Bid> findAllBid(){
        List<Bid> result = new ArrayList<>(bids.values());
        Collections.reverse(result);

        return result;
    }

    public Bid findById(String bidId){
        return bids.get(bidId);
    }

    public Bid createBid(String name){
        Bid bid = Bid.create(name);
        bids.put(bid.getBidId(), bid);
        return bid;
    }

}
