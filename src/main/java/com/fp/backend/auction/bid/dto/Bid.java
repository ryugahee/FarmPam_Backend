package com.fp.backend.auction.bid.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Bid {
    private String bidId;
    private String bidName;

    public static Bid create(String name){
        Bid bid = new Bid();
        bid.bidId = UUID.randomUUID().toString();
        bid.bidName = name;
        return bid;
    }
}
