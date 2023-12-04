package com.fp.backend.system.config.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {
    @MessageMapping("/receive")
    @SendTo("/send")
    public SocketVO SocketHandler(SocketVO socketVO){
        String userName = socketVO.getUserName();
        String content = socketVO.getContent();

        return new SocketVO(userName, content);
    }
}
