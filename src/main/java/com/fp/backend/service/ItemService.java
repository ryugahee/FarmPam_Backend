package com.fp.backend.service;


import com.fp.backend.dto.ItemFormDto;
import com.fp.backend.entity.Item;
import com.fp.backend.entity.ItemImg;
import com.fp.backend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
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

}
