package com.fp.backend.auction.bid.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BidMessage {
    private String bidId;
    private String bidPublisher;
    private int bidPrice;

}
