package com.fp.backend.user.controller;

import com.fp.backend.user.dto.UserRankDto;
import com.fp.backend.user.service.UserRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserRankController {

    private final UserRankService userRankService;

    @GetMapping("/home/rank")
    public ResponseEntity<List<UserRankDto>> getRankingList() {
        System.out.println("사용자랭킹요청");
        List<UserRankDto> top3Users = userRankService.getFindItemCount();
        System.out.println("랭킹정보: " + top3Users);
        return new ResponseEntity<>(top3Users, HttpStatus.OK);
    }

}
