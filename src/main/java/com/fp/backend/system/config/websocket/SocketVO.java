package com.fp.backend.system.config.websocket;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data

@AllArgsConstructor
public class SocketVO {
    private String bidId;
    private Object content;
}
