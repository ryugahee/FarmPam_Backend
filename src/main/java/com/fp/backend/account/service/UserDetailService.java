package com.fp.backend.account.service;

import com.fp.backend.account.entity.Users;
import com.fp.backend.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

//        System.out.println("loadUserByUsername 실행 : " + username);
//
//       Optional <Users> user = userRepository.findByUsername(username);
//
//        System.out.println("loadUserByUsername 에서 찾은 유저 : " + user.toString());
//
//       return Users.builder()
//               .username(username)
//               .password(user.get().getPassword())
//               .build();

//        try {
//            return userRepository.findByUsername(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
//        } catch (UsernameNotFoundException ex) {
//            // 사용자를 찾을 수 없는 경우, 로그인 실패 핸들러로 예외를 던집니다.
//            throw new AuthenticationServiceException("로그인 실패: 사용자를 찾을 수 없습니다.");
//        }

    }
}
