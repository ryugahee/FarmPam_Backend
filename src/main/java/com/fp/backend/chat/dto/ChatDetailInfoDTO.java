package com.fp.backend.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDetailInfoDTO {

    private String toNickName;
    private String itemTitle;
    private String itemThumbnailUrl;
    private Long biddingPrice;
}
