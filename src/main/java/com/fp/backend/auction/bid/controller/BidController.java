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
    public String bidPostResponse(@PathVariable("itemId") String id){
        String data = bidService.currentPrice(id);
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

    public void bidTimeCtrl(){

    }


//
//    @MessageMapping("/bid-update")
//    @SendTo("/bidList")
//    public SocketVO bidLastIndex(SocketVO socketVO){
//        String bidId = socketVO.getBidId();
//        System.out.println("IndexName = " + bidId);
//        Object content = socketVO.getContent();
//        System.out.println("IndexContent = " + content);
//        System.out.println("bidID = " + socketVO.getBidId());
//        return new SocketVO(bidId, content);
//    }
//    @MessageMapping("/bid-List")
//    @SendTo("/bidList")
//    public List<Object> bidList(String bidId){
//        return redisService.getValuesListAll(bidId);
//    }

//    @PostMapping("/auction/detail")
//    public bidDetail(){

//    }

//    @MessageMapping("/auction-bid")
//    @SendTo("/bidList")
//    public SocketVO pubBid(@Payload SocketVO socketVO){
//
//        String userName = socketVO.getUserName();
//        System.out.println("userName = " + userName);
//        String content = socketVO.getContent();
//        System.out.println("content = " + content);
//        redisService.setValuesPush(userName, content);
//        System.out.println("data"+redisService.getValuesListAll(userName));
//
//        return new SocketVO(userName, content);
//    }
}
