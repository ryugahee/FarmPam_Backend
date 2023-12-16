package com.fp.backend.auction.bid.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BidVO {
    private String bidId;
    private String userName;
    private Object content;
}
