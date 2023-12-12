package com.fp.backend.auction.bid.dto;


import lombok.Getter;

@Getter
public class BidData {
    private String userName;
    private String bidPrice;

    public BidData(String userName, String bidPrice){
        this.userName = userName;
        this.bidPrice = bidPrice;
    }

}
