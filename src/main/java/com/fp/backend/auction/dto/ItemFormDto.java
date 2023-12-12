package com.fp.backend.auction.dto;

import com.fp.backend.auction.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter
public class ItemFormDto {

    private Long id;

    private String itemTitle;

    private Integer minPrice;

    private String itemDetail;

    private long time;

    private String itemType;

    private int weight;

    private Boolean isSoldout;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    // 상품 이미지 아이디를 저장 (상품 수정시 이미지 저장)
    private List<Long> itemImgIds = new ArrayList<>();

    // 태그 저장
    private List<String> tagNames;

    private String userId;

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }
    public static ItemFormDto of(Item item) {
        ItemFormDto itemFormDto = modelMapper.map(item, ItemFormDto.class);

        // 남은 경매 시간
        long elapsedTime = item.getTime() - System.currentTimeMillis();
        if (elapsedTime > 0) {
            itemFormDto.setTime(elapsedTime);
            System.out.println("경매마감시간-현재시간: " + elapsedTime);
        } else {
            itemFormDto.setTime(0);
            itemFormDto.setIsSoldout(true);
        }

        return itemFormDto;

    }

    // 추가할 것 - 작성자 정보
}

