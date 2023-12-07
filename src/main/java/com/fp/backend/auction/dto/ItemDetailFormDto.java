package com.fp.backend.auction.dto;

import com.fp.backend.auction.entity.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemDetailFormDto {
    private Long id;

    private String itemTitle;

    private Integer minPrice;

    private String itemDetail;

    private long time;

    private String itemType;

    private int weight;

    private Boolean isSoldout;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

/*    public static ItemDetailFormDto of(Optional<Item> item) {
        ItemDetailFormDto itemDetailFormDto = modelMapper.map(item, ItemDetailFormDto.class);

        return itemDetailFormDto;
    }*/

    public static ItemDetailFormDto of(Item item) {
        ItemDetailFormDto itemDetailFormDto = modelMapper.map(item, ItemDetailFormDto.class);

        return itemDetailFormDto;
    }

//    public Item createItem() {
//        return modelMapper.map(this, Item.class);
//    }

    // 추가할 것 - 작성자 프로필사진, 닉네임, 별점
}
