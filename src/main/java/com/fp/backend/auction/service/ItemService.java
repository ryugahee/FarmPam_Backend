package com.fp.backend.auction.service;



import com.fp.backend.auction.bid.dto.BidData;
import com.fp.backend.account.entity.Users;
import com.fp.backend.account.repository.UserRepository;
import com.fp.backend.auction.bid.service.BidService;
import com.fp.backend.auction.dto.ItemDetailFormDto;
import com.fp.backend.auction.dto.ItemFormDto;
import com.fp.backend.auction.dto.ItemImgDto;
import com.fp.backend.auction.entity.Item;
import com.fp.backend.auction.entity.ItemImg;
import com.fp.backend.auction.entity.ItemTagMap;
import com.fp.backend.auction.repository.ItemImgRepository;
import com.fp.backend.auction.repository.ItemRepository;
import com.fp.backend.auction.repository.ItemTagMapRepository;
import com.fp.backend.auction.repository.MarketValueRepository;

import jakarta.persistence.EntityManager;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final MarketValueRepository marketValueRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemTagMapRepository itemTagMapRepository;
    private final ItemImgService itemImgService;
    private final ItemTagMapService itemTagMapService;
    private final BidService bidService;

    private final EntityManager entityManager;

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
        //Redis에 경매 시작시 등록된 정보를
        // Key:value(게시자, 경매 시작가, 경매 종료시간) 형태로 저장
        bidService.setBidRegistryInfo(
                String.valueOf(item.getId()), users.getUsername(),
                String.valueOf(item.getMinPrice()), updatedTime);


        return item.getId();
    }

    // 경매 검색
    @Transactional(readOnly = true)
    public List<ItemFormDto> getItemList(int page, String sortType, String keyword) {

        Slice<Item> itemList;

        System.out.println("타입: " + sortType);
        System.out.println("키워드: " + keyword);

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
            } else if (sortType.equals("buyer")) {
                PageRequest pageable = PageRequest.of(page, 7);
                itemList = this.itemRepository.findByBuyerIsNotNull(pageable);

            } else if (sortType.equals("completed")) {
                PageRequest pageable = PageRequest.of(page, 7);
                itemList = this.itemRepository.findCompletedItemsOrderedByIdAsc(pageable);

            } else if (sortType.equals("home")) {
                PageRequest pageable = PageRequest.of(page, 7);
                itemList = this.itemRepository.findByIsSoldoutFalseOrderByIdAsc(pageable);

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

            if (sortType.equals("completed")) {
                if (itemFormDto.getIsSoldout()) {
                    itemFormDtoList.add(itemFormDto);
                }
            } else if (sortType.equals("buyer")) {
                if (itemFormDto.getIsSoldout()) {
                    itemFormDtoList.add(itemFormDto);
                }
            } else {
                if (!itemFormDto.getIsSoldout()) {
                    itemFormDtoList.add(itemFormDto);
                }
            }

        }
        return itemFormDtoList;
    }


    @Transactional(readOnly = true)
    public Page<Item> getAllItems(int page, String sortType) {

        PageRequest pageable = PageRequest.of(page, 9);

//        return itemRepository.findAll(pageable);

        if (sortType.equals("completed")) {
            return itemRepository.findAllByIsSoldoutTrue(pageable);
        }
        else {
            return itemRepository.findAllByIsSoldoutFalse(pageable);
        }
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


    //시세 검색
    public Map<String, List<?>> searchMarketValues(String itemType) {

        String defaultSql = "SELECT \n" +
                "  item_type,\n" +
                "  GROUP_CONCAT(average_price ORDER BY sold_date SEPARATOR ',') AS prices,\n" +
                "  GROUP_CONCAT(sold_date ORDER BY sold_date SEPARATOR ',') AS dates\n" +
                "FROM (\n" +
                "  SELECT \n" +
                "    item_type,\n" +
                "    AVG(average_price) AS average_price,\n" +
                "    DATE(sold_date) AS sold_date\n" +
                "  FROM market_value\n" +
                "  WHERE sold_date BETWEEN DATE_SUB(CURDATE(), INTERVAL 30 DAY) AND CURDATE()\n" +
                "  AND item_type = :itemType \n" +
                "  GROUP BY item_type, DATE(sold_date)\n" +
                ") AS avg_prices\n" +
                "GROUP BY item_type;";

//        String defaultSql2 = "SELECT AVG(average_price) AS average_price, DATE_FORMAT(sold_date, '%Y-%m-%d') AS day, item_type\n" +
//                "FROM market_value\n" +
//                "where item_type = '배'\n" +
//                "and sold_date BETWEEN DATE_SUB(CURDATE(), INTERVAL 30 DAY) AND CURDATE()\n" +
//                "GROUP BY DATE_FORMAT(sold_date, '%Y-%m-%d'), item_type";
//
////        List<MarketValue> marketValues = marketValueRepository.findMarketValues(itemType);
//
//        String sql = "SELECT AVG(average_price) AS average_price, DATE_FORMAT(sold_date, '%Y-%m-%d') AS day, item_type\n" +
//                defaultSql;
//
//        List<MarketValue> resultList = entityManager.createNativeQuery(sql)
//                .setParameter("itemType", "%" + itemType + "%")
//                .getResultList();
//
//
//        String priceSql = "SELECT AVG(average_price) AS average_price\n" +
//                defaultSql;
//
//
//        List<Integer> priceList = entityManager.createNativeQuery(priceSql).setParameter("itemType", "" + itemType + "").getResultList();
//
//        String dateSql = "SELECT DATE_FORMAT(sold_date, '%Y-%m-%d') AS day\n" +
//                defaultSql;
//
//
//        List<String> dayList = entityManager.createNativeQuery(dateSql).setParameter("itemType", "" + itemType + "").getResultList();
//
//        String itemSql = "SELECT item_type\n" +
//                defaultSql;
//
//
//        List<String> itemList = entityManager.createNativeQuery(itemSql).setParameter("itemType", "" + itemType + "").getResultList();

        Map<String, List<?>> resultMap = new HashMap<>();
//        resultMap.put("resultList", resultList);
//        resultMap.put("priceList", priceList);
//        resultMap.put("dayList", dayList);
//        resultMap.put("itemList", itemList);

        List<Object[]> values = entityManager.createNativeQuery(defaultSql).setParameter("itemType", "" + itemType + "").getResultList();

        entityManager.close();
        for (Object[] row : values) {
            String itemName = (String) row[0];
            String[] averagePrices = ((String) row[1]).split(","); // 평균 가격들을 배열로 분리
            String[] soldDates = ((String) row[2]).split(","); // 판매 일자들을 배열로 분리

            List<String> averagePriceList = Arrays.asList(averagePrices);
            List<String> soldDateList = Arrays.asList(soldDates);

            // 각 itemType에 대한 평균 가격과 판매 일자를 Map에 추가
            resultMap.put(itemName, Arrays.asList(averagePriceList, soldDateList));


        }
        System.out.println("검색 시세 확인: " + resultMap);

        return resultMap;

    }

    public Map<String, List<?>> getAllMarketValues(int pageNum) {

        int pageSize = 4; // 한 페이지에 보여줄 아이템 수
        int offset = pageNum * pageSize; // offset 계산

        String sql = "SELECT \n" +
                "  item_type,\n" +
                "  GROUP_CONCAT(average_price ORDER BY sold_date SEPARATOR ',') AS prices,\n" +
                "  GROUP_CONCAT(sold_date ORDER BY sold_date SEPARATOR ',') AS dates\n" +
                "FROM (\n" +
                "  SELECT \n" +
                "    item_type,\n" +
                "    AVG(average_price) AS average_price,\n" +
                "    DATE(sold_date) AS sold_date\n" +
                "  FROM market_value\n" +
                "  WHERE sold_date BETWEEN DATE_SUB(CURDATE(), INTERVAL 30 DAY) AND CURDATE()\n" +
                "  GROUP BY item_type, DATE(sold_date)\n" +
                ") AS avg_prices\n" +
                "GROUP BY item_type\n" +
                "LIMIT :pageSize OFFSET :offset"; // 페이징 처리를 위한 LIMIT 및 OFFSET 사용

        List<Object[]> values = entityManager.createNativeQuery(sql)
                .setParameter("pageSize", pageSize)
                .setParameter("offset", offset)
                .getResultList();

        // 데이터 처리를 위한 Map 생성
        Map<String, List<?>> resultMap = new HashMap<>();

        // 각 item_type에 대한 결과를 Map에 추가
        for (Object[] row : values) {
            String itemType = (String) row[0];
            String[] averagePrices = ((String) row[1]).split(","); // 평균 가격들을 배열로 분리
            String[] soldDates = ((String) row[2]).split(","); // 판매 일자들을 배열로 분리

            List<String> averagePriceList = Arrays.asList(averagePrices);
            List<String> soldDateList = Arrays.asList(soldDates);

            // 각 itemType에 대한 평균 가격과 판매 일자를 Map에 추가
            resultMap.put(itemType, Arrays.asList(averagePriceList, soldDateList));
        }

        // 전체 시세 확인
        System.out.println("전체 시세 확인: " + resultMap);

        return resultMap;
    }

    //총 페이지 수 구하기
    public int findTotalPage() {
        String sql = "SELECT COUNT(DISTINCT item_type) FROM market_value";
        long totalCount = (long) entityManager.createNativeQuery(sql).getSingleResult();

        if (totalCount > Integer.MAX_VALUE) {
            throw new IllegalStateException("Total count exceeds Integer.MAX_VALUE");
        }

        int totalPage = (int) totalCount / 4 + 1;

        return totalPage;
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


    public Slice<Item> findAuctionOngoing() {

        PageRequest pageable = PageRequest.of(0, 7, Sort.by("id").descending());

        Slice<Item> onGoingItems = itemRepository.findAll(pageable);

        System.out.println("온고잉 경매 보기 : " + onGoingItems);

        return onGoingItems;

    }

}
