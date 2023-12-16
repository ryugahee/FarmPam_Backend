package com.fp.backend.auction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity
public class MarketValue {
    @Id
    private Long id;

    private String itemType;

    private int averagePrice;

    private Timestamp soldDate;

    public MarketValue(String tagName, int averagePrice, Timestamp soldDate) {
    }
}
