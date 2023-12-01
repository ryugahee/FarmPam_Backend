package com.fp.backend.service;


import com.fp.backend.dto.ItemFormDto;
import com.fp.backend.dto.ItemImgDto;
import com.fp.backend.entity.Item;
import com.fp.backend.entity.ItemImg;
import com.fp.backend.repository.ItemImgRepository;
import com.fp.backend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;
    private final ItemTagMapService itemTagMapService;


    public Long saveItem(ItemFormDto itemFormDto,
                         List<MultipartFile> itemImgFileList) throws Exception{

        // 상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0)  //첫번째 이미지일 경우 값을 Y로 함
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        // 태그 등록
        itemTagMapService.saveItemTag(item, itemFormDto.getTagNames());

        return item.getId();
    }

/*    @Transactional(readOnly = true)
    public ItemFormDto getItemList() {
        List<ItemImg> itemImgList = itemImgRepository.findAll();

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        List<Item> itemList = itemRepository.findAll();

        List<ItemFormDto> itemFormDtoList = new ArrayList<>();
        for (Item item : itemList){
            ItemFormDto itemFormDto = ItemFormDto.of(item);
            itemFormDto.setItemImgDtoList(itemImgDtoList);
            itemFormDtoList.add(itemFormDto);
        }
        return itemFormDto;
    }*/
    @Transactional(readOnly = true)
    public List<ItemFormDto> getItemList() {
        List<Item> itemList = itemRepository.findAll();

        List<ItemFormDto> itemFormDtoList = new ArrayList<>();
        for (Item item : itemList) {
            ItemFormDto itemFormDto = ItemFormDto.of(item);

            // 상품 이미지 정보 가져오기
            List<ItemImg> itemImgList = itemImgRepository.findByItemAndRepImgYn(item, "Y");
            List<ItemImgDto> itemImgDtoList = new ArrayList<>();
            for (ItemImg itemImg : itemImgList) {
                ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
                itemImgDtoList.add(itemImgDto);

                System.out.println("대표이미지: " + itemImgList);
            }
            itemFormDto.setItemImgDtoList(itemImgDtoList);

            itemFormDtoList.add(itemFormDto);
        }
        return itemFormDtoList;
    }

}
