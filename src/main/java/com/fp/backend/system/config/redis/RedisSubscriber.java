//package com.fp.backend.system.config.redis;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class RedisSubscriber implements MessageListener {
//    private final ObjectMapper objectMapper;
//    private final RedisTemplate redisTemplate;
//    private final SimpMessageSendingOperations messageSendingOperations;
//
//    @Override
//    public void onMessage(Message message, byte[] pattern){
//        try{
//            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
//            ChatMessageRequest bidMessage = objectMapper.readValue(publishMessage, ChatMessageRequest.class);
//
//            if (bidMessage.getType().equals(MessageType.TALK)) {
//                GetChatMessageResponse chatMessageResponse = new GetChatMessageResponse(roomMessage);
//                messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomId(), chatMessageResponse);
//            }
//        } catch (Exception e){
//            throw new ChatMessageNotFoundException();
//        }
//    }
//
//}
