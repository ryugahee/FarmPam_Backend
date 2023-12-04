package com.fp.backend.system.config.websocket;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocketVO {
    private String userName;
    private String content;
}
