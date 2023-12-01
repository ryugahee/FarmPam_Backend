package com.fp.backend.service;

import com.fp.backend.common.AuthorityName;
import com.fp.backend.dto.SignupDto;
import com.fp.backend.entity.Authority;
import com.fp.backend.entity.Member;
import com.fp.backend.repository.MemberRepository;
import com.fp.backend.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Member save(SignupDto dto) {

        System.out.println("회원가입 dto 확인: " + dto.getUsername() + ", " + dto.getPassword());

        if (memberRepository.findOneWithAuthoritiesByUsername(dto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어있는 유저 입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName(AuthorityName.USER.getKey()) // "ROLE_USER"
                .build();

        return memberRepository.save(
                Member.builder()
                        .username(dto.getUsername())
                        .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                        .email(dto.getEmail())
                        .age(dto.getAge())
                        .city(dto.getCity())
                        .authorities(Collections.singleton(authority))
                        .build()
        );
    }
}
