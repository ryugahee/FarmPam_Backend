package com.fp.backend.farmmoney.controller;


import com.fp.backend.farmmoney.dto.SuccessfulBidDto;
import com.fp.backend.farmmoney.service.FarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class FarmMoneyController {

    private final FarmService farmService;

    @GetMapping("/farmmoney")
    public ResponseEntity<Long> getFarmMoney(@RequestParam String userId) {

        log.info("{}의 getFarmMoney", userId);
        Long farmMoney = farmService.getFarmMoney(userId);
        return new ResponseEntity<>(farmMoney, HttpStatus.OK);
    }

    @PostMapping("/farmmoney/successfulbid")
    public ResponseEntity<Long> payFarmMoney(@RequestBody SuccessfulBidDto successfulBidDto) {

        log.info("{}의 낙찰", successfulBidDto);
        return new ResponseEntity<>(farmService.payFarmMoney(successfulBidDto), HttpStatus.OK);
    }
}
