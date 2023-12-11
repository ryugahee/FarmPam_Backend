package com.fp.backend.chat.controller;

import com.fp.backend.chat.domain.Message;
import com.fp.backend.chat.dto.*;
import com.fp.backend.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/receive/{chatId}")
    @SendTo("/send/{chatId}")
    public MessageDTO sendMessage(MessageDTO dto) {
        return dto;
    }


    // TODO: 응답 이후 vuex의 store에 user.id가 남아있는지 확인 할 것
    //  남아있다는 가정하에 return 값에 user.id는 담지 않음.
    @PostMapping("/chats")
    public ResponseEntity<Long> createChat(@RequestBody NewChatInfoDTO dto) {
        Long chatId = chatService.createChat(dto);
        return new ResponseEntity<>(chatId, HttpStatus.OK);
    }

    @GetMapping("/chats/chatIds")
    public ResponseEntity<List<Long>> getChatIds(@RequestParam String userId) {
        List<Long> chatIds = chatService.getChatIds(userId);
        return new ResponseEntity<>(chatIds, HttpStatus.OK);
    }

    @PostMapping("/chats/chatPreviewInfos")
    public ResponseEntity<List<ChatPreviewInfoDTO>> getChatPreviewInfos(@RequestBody RequestChatPreviewInfoDTO dto) {
        return new ResponseEntity<>(chatService.getChatPreviewInfos(dto.getChatIds(), dto.getUserId()), HttpStatus.OK);
    }

    @GetMapping("/chats/chatDetailInfo")
    public ResponseEntity<ChatDetailInfoDTO> getChatDetailInfo(@RequestParam Long chatId,
                                                               @RequestParam String userId) {
        ChatDetailInfoDTO dto = chatService.getChatDetailInfo(chatId, userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/chats/chatMessages")
    public ResponseEntity<List<Message>> getChatMessages(@RequestParam Long chatId) {
        List<Message> messages = chatService.getChatMessages(chatId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping("/chats/chatMessage")
    public ResponseEntity<String> sendMessages(@RequestBody SendMessageDTO dto) {
        chatService.sendMessage(dto);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
