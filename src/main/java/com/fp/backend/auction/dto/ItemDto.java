package com.fp.backend.auction.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    private Long id;

    private String itemTitle;

    private int minPrice;

    private String itemDetail;

    private int time;

    private String itemType;

    private int weight;

    private Boolean isSoldout;

}
