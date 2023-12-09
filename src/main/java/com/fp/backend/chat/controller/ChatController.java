package com.fp.backend.chat.controller;

import com.fp.backend.chat.dto.ChatDataDTO;
import com.fp.backend.chat.dto.ChatDetailInfoDTO;
import com.fp.backend.chat.dto.ChatPreviewInfoDTO;
import com.fp.backend.chat.dto.RequestChatPreviewInfoDTO;
import com.fp.backend.chat.service.ChatService;
import com.fp.backend.chat.domain.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/receive/{chatId}")
    @SendTo("/sub/send/{chatId}")
    public String sendMessage(ChatDataDTO chatDataDTO) {
        return chatDataDTO.getMessage();
    }

    @GetMapping("/chats/chatIds")
    public ResponseEntity<List<Long>> getChatIds(@RequestParam String userId) {
        List<Long> chatIds = chatService.getIds(userId);
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
}
