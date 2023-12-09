package com.fp.backend.auction.dto;

import com.fp.backend.auction.entity.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
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

    private List<String> tagNames;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemDetailFormDto of(Item item) {
        ItemDetailFormDto itemDetailFormDto = modelMapper.map(item, ItemDetailFormDto.class);

        // 남은 경매 시간
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedTimeInMillis = item.getTime() - currentTimeMillis;
        if (elapsedTimeInMillis > 0) {
            itemDetailFormDto.setTime(elapsedTimeInMillis);
            System.out.println("경매마감시간-현재시간: " + elapsedTimeInMillis);
        } else {
            itemDetailFormDto.setIsSoldout(true);  // 경매 종료된 경우 isSoldout을 true로 설정
        }

        return itemDetailFormDto;
    }


    // 추가할 것 - 작성자 프로필사진, 닉네임, 별점
}
