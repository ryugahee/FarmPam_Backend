package com.fp.backend.oauth2.service;


import com.fp.backend.account.entity.Authorities;
import com.fp.backend.account.entity.Users;
import com.fp.backend.account.enums.AuthorityName;
import com.fp.backend.account.repository.AuthoritiesRepository;
import com.fp.backend.account.repository.UserRepository;
import com.fp.backend.oauth2.provider.GoogleUserInfo;
import com.fp.backend.oauth2.provider.KakaoUserInfo;
import com.fp.backend.oauth2.provider.NaverUserInfo;
import com.fp.backend.oauth2.provider.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final AuthoritiesRepository authoritiesRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("간편 로그인 요청 : " + userRequest.getClientRegistration().getClientName()); // Google, Naver, Kakao

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            System.out.println("카톡 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else {
            System.out.println("지원하지 않는 방식이에요.");

        }

        Optional<Users> userOptional =
                userRepository.findByEmail(oAuth2UserInfo.getEmail());

        Users user;

        if (userOptional.isPresent()) { // 이미 간편가입한 유저라면

            user = userOptional.get();

            System.out.println(user.toString());

        } else { // 새로 간편가입하는 유저라면

            user = Users.builder()
                    .username(oAuth2UserInfo.getName())
                    .nickname(oAuth2UserInfo.getName())
                    .password(oAuth2UserInfo.getEmail() + oAuth2UserInfo.getProviderId())
                    .email(oAuth2UserInfo.getEmail())
                    .build();

            Authorities authorities = Authorities.builder()
                    .username(oAuth2UserInfo.getName())
                    .authority(AuthorityName.GUEST.getKey())
                    .build();

            authoritiesRepository.save(authorities);
            userRepository.save(user);

        }

        return user;

    }
}
