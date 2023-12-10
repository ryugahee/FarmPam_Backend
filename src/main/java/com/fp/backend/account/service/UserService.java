package com.fp.backend.account.service;

import com.fp.backend.account.common.AuthorityName;
import com.fp.backend.account.dto.SignupDto;
import com.fp.backend.account.entity.Authority;
import com.fp.backend.account.entity.Users;
import com.fp.backend.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Users save(SignupDto dto) {

        System.out.println("회원가입 dto 확인: " + dto.getUserName() + ", " + dto.getUserPassword());

        if (userRepository.findOneWithAuthoritiesByUsername(dto.getUserName()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어있는 유저 입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName(AuthorityName.USER.getKey()) // "ROLE_USER"
                .build();

        return userRepository.save(
                Users.builder()
                        .username(dto.getUserName())
                        .password(bCryptPasswordEncoder.encode(dto.getUserPassword()))
                        .email(dto.getUserEmail())
                        .phoneNumber(dto.getUserPhoneNumber())
                        .streetAddress(dto.getUserLocal())
                        .build()
        );
    }
}
