package com.fp.backend.user.service;

import com.fp.backend.auction.repository.ItemRepository;
import com.fp.backend.user.dto.UserRankDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRankServiceImpl implements UserRankService {

    private final ItemRepository itemRepository;

    @Override
    public List<UserRankDto> getFindItemCount() {

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        // 현재 월의 1일
        LocalDateTime startOfMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);

        // 현재 월의 마지막 날짜 (마지막 시간까지 포함)
        LocalDateTime endOfMonth = now.withDayOfMonth(now.getMonth().length(now.toLocalDate().isLeapYear()))
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);

        System.out.println("현재 월의 1일: " + startOfMonth);
        System.out.println("현재 월의 마지막 날: " + endOfMonth);

        Pageable pageable = PageRequest.of(0, 3);

        return itemRepository.findItemCountDesc(startOfMonth, endOfMonth, pageable);
    }


}
