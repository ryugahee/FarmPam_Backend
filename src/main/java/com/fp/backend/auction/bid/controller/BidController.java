package com.fp.backend.auction.bid.controller;


import com.fp.backend.auction.bid.dto.Bid;
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
        return bidService.getValuesListAll(bidId);
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
    @MessageMapping("/bid-current")
    @SendTo("/bidPost")
    public List<Bid> bidPostCurrent(){
        return bidService.currentBid();
    }

}
