//package com.fp.backend.system.config.websocket;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//import java.util.List;
//
//@Controller
//@Slf4j
//@RequiredArgsConstructor
//public class SocketController {
//
//    @MessageMapping("/auction-bid")
//    @SendTo("/auction/bids")
//    public SocketVO SocketHandler(SocketVO socketVO) {
//        String userName = socketVO.getUserName();
//        String content = socketVO.getContent();
//
//        return new SocketVO(userName, content);
//    }
//
//}