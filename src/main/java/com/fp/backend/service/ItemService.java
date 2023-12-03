package com.fp.backend.service;


import com.fp.backend.dto.ItemFormDto;
import com.fp.backend.dto.ItemImgDto;
import com.fp.backend.entity.Item;
import com.fp.backend.entity.ItemImg;
import com.fp.backend.repository.ItemImgRepository;
import com.fp.backend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

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
        // 경매 마감 시간 저장
        long updatedTime = itemFormDto.getTime() * 1000 + currentTimeMillis();
        item.setTime(updatedTime);

        item.setIsSoldout(false);

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


    @Transactional(readOnly = true)
    public List<ItemFormDto> getItemList() {
        List<Item> itemList = itemRepository.findByIsSoldout(false);
//        PageRequest pageRequest = PageRequest.of(0, size);
//        List<Item> itemList = itemRepository.findByIsSoldoutOrderByIdDesc(false, lastId, pageRequest );

        List<ItemFormDto> itemFormDtoList = new ArrayList<>();
        for (Item item : itemList) {
            ItemFormDto itemFormDto = ItemFormDto.of(item);

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
