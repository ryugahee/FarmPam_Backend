package com.fp.backend.farmmoney.service;

import com.fp.backend.account.entity.Users;
import com.fp.backend.account.repository.UserRepository;
import com.fp.backend.farmmoney.dto.SuccessfulBidDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FarmService {

    private final UserRepository userRepository;

    public Long getFarmMoney(String userId) {
        Users user = getUser(userId);

        return user.getFarmMoney();
    }

    @Transactional
    public Long payFarmMoney(SuccessfulBidDto dto) {
        Users user = getUser(dto.getUsername());

        return user.payFarmMoney(dto.getAmount());
    }

    private Users getUser(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자를 찾으려고 합니다."));
    }
}
