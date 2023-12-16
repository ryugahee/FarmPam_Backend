package com.fp.backend.auction.bid.controller;


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




    @PostMapping("/bid-list")
    @SendTo("/bidList")
    public Object bidList(@RequestBody SocketVO socketVO){
        String bidId = socketVO.getBidId();
        System.out.println("bidService.getValuesListAll(bidId) = " + bidService.getValuesListAll(bidId));
        return bidService.getValuesListAll(bidId);

    }
    @PostMapping("/bid-myPrice/{itemId}")
    public List<BidVO> MyBidPrice(@PathVariable("itemId") Long id, String userName){
        System.out.println(id);
        System.out.println(userName);
        return bidService.getMyBidPrice(String.valueOf(id), userName);
    }


    @PostMapping("/bid-finish/{itemId}")
    public void bidFinish(@PathVariable("itemId") Long id){
        Object lastBid = bidService.getValuesLastIndex(String.valueOf(id));
        System.out.println("lastBid = " + lastBid);
    }

    @GetMapping("/bidPost")
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
