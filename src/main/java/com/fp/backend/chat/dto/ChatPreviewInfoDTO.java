package com.fp.backend.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatPreviewInfoDTO {

    private String toNickName;

    private String toNickNameThumbnailUrl;

    private String lastMessage;

    private String updateTime;

    private String itemThumbnailUrl;
}
