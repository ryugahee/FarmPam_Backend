package com.fp.backend.auction.dto;

import com.fp.backend.auction.entity.Item;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@ToString
public class ItemFormDto {

    private Long id;

    private String itemTitle;

    private Integer minPrice;

    private String itemDetail;

    private long time;

    private String city;

    private int weight;

    private Boolean isSoldout;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private List<String> tagNames;

    private String userId;

    private String buyer;

    private int lastBidPrice;

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }
    public static ItemFormDto of(Item item) {
        ItemFormDto itemFormDto = modelMapper.map(item, ItemFormDto.class);

        // 경매 마감 시간
        long elapsedTime = item.getTime() - System.currentTimeMillis();
        if (elapsedTime > 0) {
            itemFormDto.setTime(elapsedTime);
        } else {
            itemFormDto.setTime(0);
            itemFormDto.setIsSoldout(true);
        }

        itemFormDto.setUserId(item.getUsers().getUsername());
        itemFormDto.setBuyer(item.getBuyer() != null ? item.getBuyer() : null);
        itemFormDto.setLastBidPrice(item.getLastBidPrice());

        return itemFormDto;

    }

}
