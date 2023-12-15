package com.fp.backend.farmmoney.controller;


import com.fp.backend.farmmoney.service.FarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
