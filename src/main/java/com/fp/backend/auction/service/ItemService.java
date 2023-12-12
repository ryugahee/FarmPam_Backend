package com.fp.backend.auction.service;



import com.fp.backend.auction.bid.dto.BidData;
import com.fp.backend.account.entity.Users;
import com.fp.backend.account.repository.UserRepository;
import com.fp.backend.auction.dto.ItemDetailFormDto;
import com.fp.backend.auction.dto.ItemFormDto;
import com.fp.backend.auction.dto.ItemImgDto;
import com.fp.backend.auction.entity.Item;
import com.fp.backend.auction.entity.ItemImg;
import com.fp.backend.auction.entity.ItemTagMap;
import com.fp.backend.auction.repository.ItemImgRepository;
import com.fp.backend.auction.repository.ItemRepository;
import com.fp.backend.auction.repository.ItemTagMapRepository;
import com.fp.backend.system.config.redis.RedisService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemTagMapRepository itemTagMapRepository;
    private final ItemImgService itemImgService;
    private final ItemTagMapService itemTagMapService;
    private final RedisService redisService;
    private final Gson gson;

    private final UserRepository userRepository;

    // 경매 등록
    @Transactional
    public Long saveItem(ItemFormDto itemFormDto,
                         List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록
        Item item = itemFormDto.createItem();
        // 경매 마감 시간 저장
        long updatedTime = itemFormDto.getTime() * 1000 + currentTimeMillis();



        item.setTime(updatedTime);
        item.setIsSoldout(false);

        Users users = userRepository.findById(itemFormDto.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않은 유저입니다."));
        item.setUsers(users);

        itemRepository.save(item);

        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i == 0)  //첫번째 이미지일 경우 값을 Y로 함
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        // 태그 등록
        itemTagMapService.saveItemTag(item, itemFormDto.getTagNames());


        String Id = String.valueOf(item.getId());
        String userName = (item.getUserName());
        System.out.println("userName = " + userName);
        String minPrice = String.valueOf((item.getMinPrice()));
        System.out.println("minPrice = " + minPrice);
        BidData bidData = new BidData(userName, minPrice);

        String data = gson.toJson(bidData);

        System.out.println("Register Data = " + data);

        redisService.setValuesPush(Id, data);

        return item.getId();
    }

    // 경매 검색
    @Transactional(readOnly = true)
    public List<ItemFormDto> getItemList(int page, String sortType, String keyword) {

        Slice<Item> itemList;

        System.out.println("타입: " + sortType);
        System.out.println("타입: " + keyword);


        if (keyword != "") {

            if (sortType.equals("time")) {
                PageRequest pageable = PageRequest.of(page, 7);
                itemList = this.itemRepository.findByKeywordAndTime(keyword, pageable);
            } else {
                PageRequest pageable = PageRequest.of(page, 7);
                itemList = this.itemRepository.findByKeywordAndLatest(keyword, pageable);
            }
        } else {
            if (sortType.equals("time")) {
                PageRequest pageable = PageRequest.of(page, 7, Sort.by("time").ascending());
                itemList = this.itemRepository.findByIsSoldoutFalseOrderByTime(pageable);
            } else {
                PageRequest pageable = PageRequest.of(page, 7, Sort.by("id").descending());
                itemList = this.itemRepository.findByIsSoldoutFalseOrderByIdDesc(pageable);
            }
        }

        List<ItemFormDto> itemFormDtoList = new ArrayList<>();
        for (Item item : itemList) {
            ItemFormDto itemFormDto = ItemFormDto.of(item);

            List<ItemImg> itemImgList = itemImgRepository.findByItemAndRepImgYn(item, "Y");
            List<ItemImgDto> itemImgDtoList = new ArrayList<>();
            for (ItemImg itemImg : itemImgList) {
                ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
                itemImgDtoList.add(itemImgDto);

            }
            itemFormDto.setItemImgDtoList(itemImgDtoList);

            if (!itemFormDto.getIsSoldout()) {
            itemFormDtoList.add(itemFormDto);
            }

            System.out.println("시간: " + itemFormDto.getTime());
            System.out.println("솔아: " + itemFormDto.getIsSoldout());

        }
        return itemFormDtoList;
    }

    //네브바 검색
    @Transactional(readOnly = true)
    public List<ItemFormDto> getItemList(int page, String keyword) {

        Slice<Item> itemList;

        System.out.println("타입: " + keyword);

        if (keyword != "") {
            PageRequest pageable = PageRequest.of(page, 7);
            itemList = this.itemRepository.findByKeywordAndLatest(keyword, pageable);
        } else {
            PageRequest pageable = PageRequest.of(page, 7, Sort.by("time").ascending());
            itemList = this.itemRepository.findByIsSoldoutFalseOrderByTime(pageable);
        }

        List<ItemFormDto> itemFormDtoList = new ArrayList<>();
        for (Item item : itemList) {

            ItemFormDto itemFormDto = ItemFormDto.of(item);

            List<ItemImg> itemImgList = itemImgRepository.findByItemAndRepImgYn(item, "Y");
            List<ItemImgDto> itemImgDtoList = new ArrayList<>();
            for (ItemImg itemImg : itemImgList) {
                ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
                itemImgDtoList.add(itemImgDto);

            }
            itemFormDto.setItemImgDtoList(itemImgDtoList);

            if (!itemFormDto.getIsSoldout()) {
                itemFormDtoList.add(itemFormDto);
            }

        }
        return itemFormDtoList;
    }


    // 경매 삭제
    @Transactional
    public ItemFormDto delete(Long id) {
        Item target = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품 삭제 실패!"));

        // 상품 삭제
        itemRepository.delete(target);

        // 상품 이미지 삭제
        List<ItemImg> itemImgList = itemImgRepository.findByItem(target);
        for (ItemImg itemImg : itemImgList) {
            itemImgRepository.delete(itemImg);
        }

        // 상품 태그 삭제
        itemTagMapService.deleteItemTagByItem(target);

        return ItemFormDto.of(target);

    }

    // 경매 디테일
    @Transactional(readOnly = true)
    public ItemDetailFormDto getItemDetail(Long id) {

        Optional<Item> itemOptional = itemRepository.findById(id);
        Item item = itemOptional.orElse(null);

        ItemDetailFormDto itemDetailFormDto = ItemDetailFormDto.of(item);

        // 이미지 불러옴
        List<ItemImg> itemImgList = itemImgRepository.findByItem(item);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
        itemDetailFormDto.setItemImgDtoList(itemImgDtoList);

        // 태그 불러옴
        List<ItemTagMap> itemTagMaps = itemTagMapRepository.findByItem(item);
        List<String> tagNames = itemTagMaps.stream()
                .map(itemTagMap -> itemTagMap.getItemTag().getTagName())
                .collect(Collectors.toList());
        itemDetailFormDto.setTagNames(tagNames);

        return itemDetailFormDto;

    }

    // 스케줄러
    public void updateExpiredItems() {
        long currentTimeMillis = System.currentTimeMillis();
        List<Item> expiredItems = itemRepository.findByTimeLessThan(currentTimeMillis);

        System.out.println("현재시간: " + currentTimeMillis());

        for (Item item : expiredItems) {
            if (item.getTime() < System.currentTimeMillis()) {
                item.setTime(0);
                item.setIsSoldout(true);
            }
        }
        itemRepository.saveAll(expiredItems);
    }

    public String getSellerId(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("존재하지 않은 경매 입니다."));

        return item.getUsers().getUsername();
    }
}
