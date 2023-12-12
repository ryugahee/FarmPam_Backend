package com.fp.backend.auction.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    private Long id;

    private String userName;

    private String itemTitle;

    private int minPrice;

    private String itemDetail;

    private long time;

    private String city;

    private int weight;

    private Boolean isSoldout;

}
