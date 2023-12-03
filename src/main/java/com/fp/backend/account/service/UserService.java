package com.fp.backend.account.service;

import com.fp.backend.account.common.AuthorityName;
import com.fp.backend.account.dto.SignupDto;
import com.fp.backend.account.entity.Authority;
import com.fp.backend.account.entity.Member;
import com.fp.backend.account.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Member save(SignupDto dto) {

        System.out.println("회원가입 dto 확인: " + dto.getUserName() + ", " + dto.getUserPassword());

        if (memberRepository.findOneWithAuthoritiesByUserName(dto.getUserName()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어있는 유저 입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName(AuthorityName.USER.getKey()) // "ROLE_USER"
                .build();

        return memberRepository.save(
                Member.builder()
                        .userName(dto.getUserName())
                        .userPassword(bCryptPasswordEncoder.encode(dto.getUserPassword()))
                        .userEmail(dto.getUserEmail())
                        .userPhoneNumber(dto.getUserPhoneNumber())
                        .userLocal(dto.getUserLocal())
                        .authorities(Collections.singleton(authority))
                        .build()
        );
    }
}
