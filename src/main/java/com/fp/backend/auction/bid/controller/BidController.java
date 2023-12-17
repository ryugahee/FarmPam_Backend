package com.fp.backend.auction.bid.controller;


import com.fp.backend.account.entity.Users;
import com.fp.backend.account.service.UserService;
import com.fp.backend.auction.bid.dto.Bid;
import com.fp.backend.auction.bid.dto.BidVO;
import com.fp.backend.auction.bid.service.BidService;
import com.fp.backend.system.config.websocket.SocketVO;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class BidController {

    private final BidService bidService;
    private final UserService userService;

    @PostMapping("/publisherInfo")
    public String bidPublisher(@RequestBody SocketVO socketVO){
        System.out.println("userName = " + socketVO.getBidId());

        Users users = userService.getUserInfo(socketVO.getBidId());
        System.out.println("users.getNickname() = " + users.getNickname());
        return users.getNickname();
    }
    @PostMapping("/bid-list")
    @SendTo("/bidList")
    public Object bidList(@RequestBody SocketVO socketVO){
        String bidId = socketVO.getBidId();
        System.out.println("bidService.getValuesListAll(bidId) = " + bidService.getValuesListAll(bidId));
        return bidService.getValuesListAll(bidId);

    }
    @PostMapping("/bid-myPrice")
    public List<BidVO> MyBidPrice( @RequestBody SocketVO socketVO){
        System.out.println("여기 존나 여기: "+socketVO.getBidId());
        System.out.println("여기 존나 여기 2: "+socketVO.getContent());
        List<BidVO> data = bidService.getMyBidPrice(socketVO.getBidId(), (String) socketVO.getContent());
        System.out.println("존나존나 여기인듯한여기: "+ data.get(0));

        return data;
    }


    @PostMapping("/bid-finish/{itemId}")
    public void bidFinish(@PathVariable("itemId") Long id){
        Object lastBid = bidService.getValuesLastIndex(String.valueOf(id));
        System.out.println("lastBid = " + lastBid);
    }

    @GetMapping("/bid-Post")
    public Object bidPostResponse(){
        return bidService.currentBid();
    }

    @MessageMapping("/bid-push")
    @SendTo("/bidList")
    public Object bidPush(@Payload SocketVO socketVO){
        String bidId = socketVO.getBidId();
        Object content = socketVO.getContent();
        bidService.setValuesPush(bidId, content);

        return bidService.getValuesListAll(bidId);
    }
//    @MessageMapping("/bid-myPrice")
//    @SendTo("/bidList")
//    public Object myBidPrice(@Payload SocketVO socketVO){
//        String bidId = socketVO.getBidId();
//        Object content = socketVO.getContent();
//        bidService.myBidPrice(bidId, content);
//
//    }


    @MessageMapping("/bid-current")
    @SendTo("/bidPost")
    public List<Bid> bidPostCurrent(){
        return bidService.currentBid();
    }



}
