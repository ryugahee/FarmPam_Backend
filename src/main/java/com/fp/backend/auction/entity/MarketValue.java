package com.fp.backend.auction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class MarketValue {
    @Id
    private Long id;

    @Column
    private String itemType;

    @Column
    private int averagePrice;

    @Column
    private Timestamp soldDate;


    public MarketValue(String tagName, int averagePrice, Timestamp soldDate) {
        this.itemType = tagName;
        this.averagePrice = averagePrice;
        this.soldDate = soldDate;
    }
}
