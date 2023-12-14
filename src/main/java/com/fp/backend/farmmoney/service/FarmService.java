package com.fp.backend.farmmoney.service;

import com.fp.backend.account.entity.Users;
import com.fp.backend.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FarmService {

    private final UserRepository userRepository;

    public Long getFarmMoney(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않은 유저 입니다."));

        return user.getFarmMoney();
    }
}
