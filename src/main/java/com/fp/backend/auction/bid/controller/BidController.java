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
        System.out.println("bidId = " + bidId);
        return bidService.getValuesListAll(bidId);
    }
    @PostMapping("/bid-finish/{itemId}")
    public void bidFinish(@PathVariable("itemId") Long id){
        Object lastBid = bidService.getValuesLastIndex(String.valueOf(id));
        System.out.println("lastBid = " + lastBid);
    }

    @GetMapping("/bidPost/{itemId}")
    public Object bidPostResponse(@PathVariable("itemId") String id){
        Object data = bidService.currentPrice(id);
        System.out.println("currentBidPrice = " + data);
        return data;
    }
    @MessageMapping("/bid-push")
    @SendTo("/bidList")
    public Object bidPush(@Payload SocketVO socketVO){
        String bidId = socketVO.getBidId();
        System.out.println("bidId = " + bidId);
        Object content = socketVO.getContent();
        System.out.println("content = " + content);


        bidService.setValuesPush(bidId, content);
        return bidService.getValuesListAll(bidId);
    }

}
