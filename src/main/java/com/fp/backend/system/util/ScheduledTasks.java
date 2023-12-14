package com.fp.backend.system.util;

import com.fp.backend.auction.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {

    @Autowired
    public ScheduledTasks(ItemService itemService) {
        this.itemService = itemService;
    }

    ItemService itemService;

    // 정각마다 업데이트
//    @Scheduled(cron = "0 0 0/1 * * ?")
//    public void updateExpiredItems() {
//
//        log.info("경매 마감 시간 업데이트 시작");
//        itemService.updateExpiredItems();
//        log.info("경매 마감 시간 업데이트 종료");
//
//    }
    @Scheduled(fixedRate = 5000)
    public void updateExpiredItems() {

        log.info("경매 마감 시간 업데이트 시작");
        itemService.updateExpiredItems();
        log.info("경매 마감 시간 업데이트 종료");

    }
}
