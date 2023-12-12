package com.fp.backend.auction.dto;

import lombok.Getter;

import java.sql.Timestamp;

@Getter

public class ItemMarketValueDto {

    private String itemType;

    private int averagePrice;

    private Timestamp soldDate;

}
