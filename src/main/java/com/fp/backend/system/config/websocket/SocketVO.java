package com.fp.backend.system.config.websocket;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SocketVO {
    public enum MessageType{
        ENTER, TALK, LEAVE
    }
    private MessageType type;
    private String roomId;
    private String publisher;
    private String userName;
    private String content;
    private String time;
}
