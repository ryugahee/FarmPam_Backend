package com.fp.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

    private boolean isSoldout;

}
