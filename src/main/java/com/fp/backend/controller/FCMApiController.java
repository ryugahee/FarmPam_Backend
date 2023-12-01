package com.fp.backend.controller;

import com.fp.backend.dto.FCMRequestDto;
import com.fp.backend.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Tag(name = "Notification", description = "FCM Notification api ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/f1/notification")
public class FCMApiController {
//    private final FCMService fcmService;
//
//    @Operation(summary = "알림보내기")
//    @PostMapping()
//    public String sendByToken(@RequestBody FCMRequestDto requestDto){
//        return fcmService.sendByToken(requestDto);
//    }
}
