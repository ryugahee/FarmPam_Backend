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
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("bidPrice")
    @Expose
    private String bidPrice;

    public Bid(String userName, String bidPrice){
        this.userName = userName;
        this.bidPrice = bidPrice;
    }
}
