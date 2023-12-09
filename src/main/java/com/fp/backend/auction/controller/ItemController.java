package com.fp.backend.auction.controller;

import com.fp.backend.auction.dto.ItemDetailFormDto;
import com.fp.backend.auction.dto.ItemFormDto;
import com.fp.backend.auction.entity.Item;
import com.fp.backend.auction.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;

    // 경매 등록
    @PostMapping("/item/new")
    public ResponseEntity<String> itemNew(@Valid ItemFormDto itemFormDto,
                                          @RequestParam("files") List<MultipartFile> itemImgFileList) {

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

    // 최신순 리스트
    @GetMapping("/item/list")
    public ResponseEntity<List<ItemFormDto>> getItemList(@RequestParam("num") Long num) {
        System.out.println("최신 리스트 요청: " + num);
        List<ItemFormDto> itemList = itemService.getItemList(num);
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
    };

    // 경매 디테일
    @GetMapping("/item/detail/{id}")
    public ResponseEntity<ItemDetailFormDto> getItemDetail(@PathVariable("id") Long id) {
        System.out.println("경매 디테일 요청: " + id);
        ItemDetailFormDto itemDetail = itemService.getItemDetail(id);
        return new ResponseEntity<>(itemDetail, HttpStatus.OK);
    }


}
