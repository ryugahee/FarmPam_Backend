package com.fp.backend.auction.controller;

import com.fp.backend.auction.dto.ItemDetailFormDto;
import com.fp.backend.auction.dto.ItemFormDto;

import com.fp.backend.auction.dto.ItemMarketValueDto;
import com.fp.backend.auction.entity.Item;
import com.fp.backend.auction.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.fp.backend.auction.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    // 경매 등록
    @PostMapping("/item/new")
    public ResponseEntity<String> itemNew(@Valid ItemFormDto itemFormDto,
                                          @RequestParam("files") List<MultipartFile> itemImgFileList) {
        if (itemImgFileList.size() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미지는 최대 5개까지 업로드 가능합니다.");
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("첫번째 상품 이미지는 필수 입력 값입니다.");
        }
        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 등록 중 에러가 발생했습니다.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 경매 검색
    @GetMapping("/item/list")
    public ResponseEntity<List<ItemFormDto>> getItemList(@RequestParam int page,
                                                         @RequestParam(required = false) String sortType,
                                                         @RequestParam(value = "keyword", required = false) String keyword) {

        if (keyword == null) {
            keyword = "";
        }
        List<ItemFormDto> itemList = itemService.getItemList(page, sortType, keyword);

        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    //경매 가져오기(관리자 페이지)
    @GetMapping("/item/allList")
    public ResponseEntity<?> getAllItemList(
            @RequestParam int page,
            @RequestParam String sortType
    ) {

//        Map<String, Object> response = new HashMap<>();
        Page<Item> items = itemService.getAllItems(page, sortType);

//     int pageNum =  items.getTotalPages();
//
//        response.put("items", items); // Page<Item> 추가
//        response.put("pageNum", pageNum); // int pageNum 추가

        return new ResponseEntity<>(items, HttpStatus.OK);
    }


    // 네브바 검색
    @GetMapping("/nav/item/list")
    public ResponseEntity<List<ItemFormDto>> getItemList(@RequestParam int page,
                                                         @RequestParam(value = "keyword", required = false) String keyword) {

        if (keyword == null) {
            keyword = "";
        }
        List<ItemFormDto> itemList = itemService.getItemList(page, keyword);

        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    // 경매 삭제
    @DeleteMapping("/item/delete/{itemId}")
    public ResponseEntity<ItemFormDto> deleteItem(@PathVariable("itemId") Long id) {
        System.out.println("삭제 요청: " + id);
        ItemFormDto itemDeleted = itemService.delete(id);

        if (itemDeleted != null) {
            return new ResponseEntity<>(itemDeleted, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // 경매 디테일
    @GetMapping("/item/detail/{id}")
    public ResponseEntity<ItemDetailFormDto> getItemDetail(@PathVariable("id") Long id) {
        System.out.println("경매 디테일 요청: " + id);
        ItemDetailFormDto itemDetail = itemService.getItemDetail(id);
        return new ResponseEntity<>(itemDetail, HttpStatus.OK);
    }


    //시세 검색
    @PostMapping("/item/marketValue")
    public ResponseEntity<Map<String, List<?>>> getItemMarketValue(@RequestBody ItemMarketValueDto itemType) {

        System.out.println("시세 검색 컨트롤러 진입");

        System.out.println("키워드 확인 : " + itemType.getItemType());

        if (itemType.getItemType().equals("")) {
          return getAllMarketValues(0);
        }

        Map<String, List<?>> marketValues = itemService.searchMarketValues(itemType.getItemType());

        System.out.println(marketValues);

        return new ResponseEntity<>(marketValues, HttpStatus.OK);
    }


    //전체 시세 조회
    @GetMapping("/item/allMarketValues")
    public ResponseEntity<Map<String, List<?>>> getAllMarketValues(@RequestParam int pageNum) {

        System.out.println("전체 시세 조회 컨트롤러 진입, 페이지: " + pageNum);

        Map<String, List<?>> resultMap = itemService.getAllMarketValues(pageNum);

        int totalPage = itemService.findTotalPage();

        System.out.println("토탈 페이지 : "+ totalPage);

        List<Integer> totalPageList = new ArrayList<>();

        totalPageList.add(totalPage);

        resultMap.put("totalPage", totalPageList);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }


    @GetMapping("/item/detail/{id}/seller")
    public ResponseEntity<String> getSellerId(@PathVariable Long id) {
        log.info("getSellerId {}", id);

        String sellerId = itemService.getSellerId(id);


        return new ResponseEntity<>(sellerId, HttpStatus.OK);
    }

    //관리자 페이지 진행중 경매 조회
    @GetMapping("/item/getAuctionOngoing")
    public ResponseEntity<Slice<Item>> auctionOngoing() {

        Slice<Item> onGoingItems =  itemService.findAuctionOngoing();

        return new ResponseEntity<>(onGoingItems, HttpStatus.OK);
    }

}


