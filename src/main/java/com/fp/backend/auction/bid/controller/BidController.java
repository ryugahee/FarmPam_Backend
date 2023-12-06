//package com.fp.backend.auction.bid.controller;
//
//
//import com.fp.backend.auction.bid.Service.BidService;
//import com.fp.backend.system.config.websocket.SocketVO;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/api")
//public class BidController {
//
//    private final BidService bidService;
//
//
//    @MessageMapping("/auction-bid")
//    @SendTo("/auction/bids")
//    public SocketVO SocketHandler(SocketVO socketVO){
//        String userName = socketVO.getUserName();
//        String content = socketVO.getContent();
//
//        return new SocketVO(userName, content);
//    }
//}
