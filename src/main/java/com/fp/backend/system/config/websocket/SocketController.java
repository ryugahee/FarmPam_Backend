package com.fp.backend.system.config.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SocketController {
    private final SimpMessageSendingOperations template;
    private final ChatRepository repository;

    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload SocketVO chat, SimpMessageHeaderAccessor headerAccessor){

        repository.increaseUser(chat.getRoomId());

        String userUUID = repository.addUser(chat.getRoomId(), chat.getUserName());

        headerAccessor.getSessionAttributes().put("userUUID",userUUID);
        headerAccessor.getSessionAttributes().put("roomId",chat.getRoomId());

        chat.setContent(chat.getUserName() + "님이 입장하셨습니다.");
        template.convertAndSend("/sub/chat/room/"+chat.getRoomId(),chat);
    }
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload SocketVO chat){

        log.info("chat : {}",chat);
        chat.setContent(chat.getContent());
        template.convertAndSend("/sub/chat/room/"+chat.getRoomId(),chat);
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event){

        log.info("DisconnectEvent : {}",event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 uuid 와 roomId 를 확인하여 채팅방 유저 리스트와 room에서 해당 유저를 삭제
        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor : {}",headerAccessor);

        // 채팅방 유저 -1
        repository.decreaseUser(roomId);

        //채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
        String userName = repository.getUserName(roomId, userUUID);
        repository.deleteUser(roomId,userUUID);

        if(userName != null){
            log.info("User Disconnected : " + userName);

            SocketVO chat = SocketVO.builder()
                    .type(SocketVO.MessageType.LEAVE)
                    .userName(userName)
                    .content(userName + "님이 퇴장하였습니다.")
                    .build();

            template.convertAndSend("/sub/chat/room/" + roomId,chat);
        }
    }

    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/chat/uselist")
    @ResponseBody
    public List<String> userList(String roomId){

        return repository.getUserList(roomId);
    }

    // 채팅에 참여한 유저 닉네임 중복 확인
    @GetMapping("/chat/duplicateName")
    @ResponseBody
    public String isDuplicateName(@RequestParam("roomId")String roomId ,
                                  @RequestParam("username")String username){

        String userName = repository.isDuplicateName(roomId, username);
        log.info("DuplicateName : {}", userName);

        return userName;
    }


}
//    @MessageMapping("/auction-bid")
//    @SendTo("/auction/bids")
//    public SocketVO SocketHandler(SocketVO socketVO){
//        String userName = socketVO.getUserName();
//        String content = socketVO.getContent();
//
//        return new SocketVO(userName, content);
//    }

