package com.fp.backend.dto;

import com.fp.backend.entity.Item;
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

    private int time;

    private String itemType;

    private int weight;

    private boolean isSoldout;

    // 상품 저장 후 수정할 때 상품 이미지 정보 저장 리스트
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    // 상품 이미지 아이디를 저장 (상품 수정시 이미지 저장)
    private List<Long> itemImgIds = new ArrayList<>();

    // 태그 저장
    private List<String> tagNames;

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }
    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
}
