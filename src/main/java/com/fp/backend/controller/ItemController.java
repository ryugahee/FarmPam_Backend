package com.fp.backend.controller;

import com.fp.backend.dto.ItemFormDto;
import com.fp.backend.repository.ItemRepository;
import com.fp.backend.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {

    @Autowired
    private final ItemService itemService;

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


}
