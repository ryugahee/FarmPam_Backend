package com.fp.backend.auction.bid.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Bid {
    @SerializedName("bidId")
    @Expose
    private String bidId;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("bidPrice")
    @Expose
    private String bidPrice;
    @SerializedName("bidTime")
    @Expose
    private String bidTime;

    public Bid(String bidId, String userName, String bidPrice, String bidTime){
        this.bidId = bidId;
        this.userName = userName;
        this.bidPrice = bidPrice;
        this.bidTime = bidTime;
    }
}
