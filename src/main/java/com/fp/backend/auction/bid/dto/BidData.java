package com.fp.backend.auction.bid.dto;


import lombok.Getter;

@Getter
public class BidData {
    private String userName;
    private String bidPrice;
    private long bidTime;

    public BidData(String userName, String bidPrice, long bidTime){
        this.userName = userName;
        this.bidPrice = bidPrice;
        this.bidTime = bidTime;
    }

}
